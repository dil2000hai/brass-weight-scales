package com.fulcrum.brassweights.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.ShapeType
import com.fulcrum.brassweights.domain.repository.ProgressRepository
import com.fulcrum.brassweights.domain.usecase.BuildHintUseCase
import com.fulcrum.brassweights.domain.usecase.EvaluateBalanceUseCase
import com.fulcrum.brassweights.domain.usecase.LoadPuzzleUseCase
import com.fulcrum.brassweights.domain.usecase.SaveResultUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface GameEvent {
    data object Clink : GameEvent
    data class Finished(val solved: Boolean, val moves: Int, val hints: Int) : GameEvent
}

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val loadPuzzle: LoadPuzzleUseCase,
    private val evaluateBalance: EvaluateBalanceUseCase,
    private val buildHint: BuildHintUseCase,
    private val saveResult: SaveResultUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var roundJob: Job? = null
    private var settleJob: Job? = null

    fun onScreenEnter() {
        val current = _state.value
        val finished = current.phase == GamePhase.SOLVED || current.phase == GamePhase.ABANDONED
        if (current.puzzle == null || finished) {
            openPuzzle()
        }
    }

    fun pause() {
        roundJob?.cancel()
        settleJob?.cancel()
    }

    fun onTraySelected(shape: ShapeType) {
        if (!_state.value.interactive) return
        _state.update { it.copy(selectedShape = shape) }
    }

    fun onTrayTapped(shape: ShapeType) {
        val current = _state.value
        if (!current.interactive || current.puzzle == null) return
        if (current.rightPan.size >= GameConfig.MAX_PER_PAN) return
        applyPan(current.rightPan + shape, shape)
    }

    fun onRemoveLast() {
        val current = _state.value
        if (!current.interactive || current.rightPan.isEmpty()) return
        applyPan(current.rightPan.dropLast(1), current.selectedShape)
    }

    fun onUndoAll() {
        val current = _state.value
        if (!current.interactive || current.rightPan.isEmpty()) return
        applyPan(emptyList(), current.selectedShape)
    }

    fun onCheck() {
        val current = _state.value
        val puzzle = current.puzzle ?: return
        if (!current.interactive || current.rightPan.isEmpty()) return
        val balance = evaluateBalance(puzzle, current.rightPan)
        roundJob?.cancel()
        roundJob = viewModelScope.launch {
            _state.update { it.copy(phase = GamePhase.CHECKING) }
            delay(GameConfig.CHECK_LOCK_MS)
            if (balance.solved) {
                _state.update { it.copy(phase = GamePhase.SOLVED) }
                eventChannel.send(GameEvent.Clink)
                delay(GameConfig.SOLVE_GLOW_MS)
                val finished = _state.value
                saveResult(puzzle, true, finished.moves, finished.hints, finished.deductions)
                eventChannel.send(GameEvent.Finished(true, finished.moves, finished.hints))
            } else {
                _state.update { it.copy(phase = GamePhase.UNBALANCED) }
                delay(GameConfig.UNBALANCED_MS)
                _state.update {
                    it.copy(
                        phase = if (it.rightPan.isEmpty()) GamePhase.IDLE else GamePhase.PLACING
                    )
                }
            }
        }
    }

    fun onHintRequested() {
        val current = _state.value
        if (current.puzzle == null) return
        val maxSteps = current.deductions.size + 1
        _state.update {
            it.copy(
                hintVisible = true,
                revealedHints = (it.revealedHints + 1).coerceAtMost(maxSteps),
                hints = it.hints + 1
            )
        }
    }

    fun onHintDismissed() {
        _state.update { it.copy(hintVisible = false) }
    }

    fun onToggleEquation() {
        _state.update { it.copy(showEquation = !it.showEquation) }
    }

    fun onPauseOpened() {
        _state.update { it.copy(pauseVisible = true) }
    }

    fun onPauseDismissed() {
        _state.update { it.copy(pauseVisible = false) }
    }

    fun onRestart() {
        pause()
        openPuzzle()
    }

    fun onGiveUp() {
        val current = _state.value
        val puzzle = current.puzzle ?: return
        pause()
        roundJob = viewModelScope.launch {
            _state.update { it.copy(phase = GamePhase.ABANDONED, pauseVisible = false) }
            saveResult(puzzle, false, current.moves, current.hints, current.deductions)
            eventChannel.send(GameEvent.Finished(false, current.moves, current.hints))
        }
    }

    private fun openPuzzle() {
        val index = progressRepository.progress.value.currentPuzzle
        val puzzle = loadPuzzle(index)
        _state.value = GameUiState(
            puzzle = puzzle,
            selectedShape = puzzle.targetShape,
            deductions = buildHint(puzzle),
            puzzleInChapter = index % GameConfig.PUZZLES_PER_CHAPTER + 1,
            chapterSize = GameConfig.PUZZLES_PER_CHAPTER
        )
    }

    private fun applyPan(nextPan: List<ShapeType>, selected: ShapeType?) {
        val current = _state.value
        val puzzle = current.puzzle ?: return
        val balance = evaluateBalance(puzzle, nextPan)
        _state.update {
            it.copy(
                rightPan = nextPan,
                selectedShape = selected ?: it.selectedShape,
                angle = balance.angleDegrees,
                moves = it.moves + 1,
                phase = if (nextPan.isEmpty()) GamePhase.IDLE else GamePhase.PLACING,
                tilt = when {
                    balance.leftSum > balance.rightSum -> TiltSide.LEFT
                    balance.rightSum > balance.leftSum -> TiltSide.RIGHT
                    else -> TiltSide.NONE
                }
            )
        }
        settleJob?.cancel()
        settleJob = viewModelScope.launch {
            delay(GameConfig.BEAM_SETTLE_MS)
            eventChannel.send(GameEvent.Clink)
        }
    }
}
