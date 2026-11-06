package com.fulcrum.brassweights.data.local

import android.content.Context
import android.content.SharedPreferences
import com.fulcrum.brassweights.domain.model.AppSettings
import com.fulcrum.brassweights.domain.model.GradeLevel
import com.fulcrum.brassweights.domain.model.Progress
import com.fulcrum.brassweights.domain.model.SkillTag

class PreferencesStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readProgress(): Progress = Progress(
        solvedCount = prefs.getInt(KEY_SOLVED, 0),
        currentPuzzle = prefs.getInt(KEY_CURRENT, 0),
        bestStreak = prefs.getInt(KEY_BEST_STREAK, 0),
        currentStreak = prefs.getInt(KEY_STREAK, 0),
        hintsUsed = prefs.getInt(KEY_HINTS, 0),
        masteryBySkill = decodeMastery(prefs.getString(KEY_MASTERY, "").orEmpty())
    )

    fun writeProgress(progress: Progress) {
        prefs.edit()
            .putInt(KEY_SOLVED, progress.solvedCount)
            .putInt(KEY_CURRENT, progress.currentPuzzle)
            .putInt(KEY_BEST_STREAK, progress.bestStreak)
            .putInt(KEY_STREAK, progress.currentStreak)
            .putInt(KEY_HINTS, progress.hintsUsed)
            .putString(KEY_MASTERY, encodeMastery(progress.masteryBySkill))
            .apply()
    }

    fun readSettings(): AppSettings = AppSettings(
        sound = prefs.getBoolean(KEY_SOUND, true),
        haptics = prefs.getBoolean(KEY_HAPTICS, true),
        showSymbols = prefs.getBoolean(KEY_SYMBOLS, false),
        gradeLevel = runCatching {
            GradeLevel.valueOf(prefs.getString(KEY_GRADE, GradeLevel.MIDDLE.name).orEmpty())
        }.getOrDefault(GradeLevel.MIDDLE)
    )

    fun writeSettings(settings: AppSettings) {
        prefs.edit()
            .putBoolean(KEY_SOUND, settings.sound)
            .putBoolean(KEY_HAPTICS, settings.haptics)
            .putBoolean(KEY_SYMBOLS, settings.showSymbols)
            .putString(KEY_GRADE, settings.gradeLevel.name)
            .apply()
    }

    private fun encodeMastery(mastery: Map<SkillTag, Int>): String =
        mastery.entries.joinToString(separator = ";") { "${it.key.name}=${it.value}" }

    private fun decodeMastery(raw: String): Map<SkillTag, Int> {
        if (raw.isBlank()) return emptyMap()
        return raw.split(";").mapNotNull { entry ->
            val parts = entry.split("=")
            if (parts.size != 2) return@mapNotNull null
            val tag = runCatching { SkillTag.valueOf(parts[0]) }.getOrNull() ?: return@mapNotNull null
            val value = parts[1].toIntOrNull() ?: return@mapNotNull null
            tag to value
        }.toMap()
    }

    private companion object {
        const val STORE_NAME = "brass_weight_scales"
        const val KEY_SOLVED = "solved_count"
        const val KEY_CURRENT = "current_puzzle"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_STREAK = "current_streak"
        const val KEY_HINTS = "hints_used"
        const val KEY_MASTERY = "mastery_by_skill"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_SYMBOLS = "show_symbols"
        const val KEY_GRADE = "grade_level"
    }
}
