package com.fulcrum.brassweights.data.sample

import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.Chapter
import com.fulcrum.brassweights.domain.model.ClueScale
import com.fulcrum.brassweights.domain.model.Puzzle
import com.fulcrum.brassweights.domain.model.ShapeType
import com.fulcrum.brassweights.domain.model.SkillTag

private data class Relation(
    val leftCount: Int,
    val leftRole: Int,
    val rightCount: Int,
    val rightRole: Int
)

private data class Blueprint(
    val chapter: Chapter,
    val skill: SkillTag,
    val ratios: List<Int>,
    val relations: List<Relation>,
    val leftRoles: List<Int>,
    val targetRole: Int,
    val brassRole: Int = -1
)

object SampleData {

    val sandboxWeights: Map<ShapeType, Int> = mapOf(
        ShapeType.TRIANGLE to 1,
        ShapeType.SQUARE to 2,
        ShapeType.CIRCLE to 3,
        ShapeType.HEXAGON to 6,
        ShapeType.BRASS_WEIGHT to 1
    )

    val sandboxTray: List<ShapeType> = listOf(
        ShapeType.TRIANGLE,
        ShapeType.SQUARE,
        ShapeType.CIRCLE,
        ShapeType.HEXAGON,
        ShapeType.BRASS_WEIGHT
    )

    val puzzles: List<Puzzle> by lazy { buildPuzzles() }

    private val shapeOrder = listOf(
        ShapeType.TRIANGLE,
        ShapeType.SQUARE,
        ShapeType.CIRCLE,
        ShapeType.HEXAGON
    )

    private val bench: List<List<Blueprint>> = listOf(
        listOf(
            Blueprint(
                chapter = Chapter.FIRST_BALANCE,
                skill = SkillTag.SUBSTITUTION,
                ratios = listOf(1, 2),
                relations = listOf(Relation(2, 0, 1, 1)),
                leftRoles = listOf(1),
                targetRole = 0
            ),
            Blueprint(
                chapter = Chapter.FIRST_BALANCE,
                skill = SkillTag.SUBSTITUTION,
                ratios = listOf(1, 3),
                relations = listOf(Relation(3, 0, 1, 1)),
                leftRoles = listOf(1),
                targetRole = 0
            )
        ),
        listOf(
            Blueprint(
                chapter = Chapter.TWIN_SHAPES,
                skill = SkillTag.SUBSTITUTION,
                ratios = listOf(1, 2, 2),
                relations = listOf(Relation(2, 0, 1, 1), Relation(1, 1, 1, 2)),
                leftRoles = listOf(2),
                targetRole = 0
            ),
            Blueprint(
                chapter = Chapter.TWIN_SHAPES,
                skill = SkillTag.SUBSTITUTION,
                ratios = listOf(1, 4, 4),
                relations = listOf(Relation(4, 0, 1, 1), Relation(1, 1, 1, 2)),
                leftRoles = listOf(2),
                targetRole = 0
            )
        ),
        listOf(
            Blueprint(
                chapter = Chapter.DOUBLE_UP,
                skill = SkillTag.DOUBLING,
                ratios = listOf(1, 2, 4),
                relations = listOf(Relation(2, 0, 1, 1), Relation(2, 1, 1, 2)),
                leftRoles = listOf(2),
                targetRole = 0
            ),
            Blueprint(
                chapter = Chapter.DOUBLE_UP,
                skill = SkillTag.DOUBLING,
                ratios = listOf(1, 2, 6),
                relations = listOf(Relation(2, 0, 1, 1), Relation(3, 1, 1, 2)),
                leftRoles = listOf(2),
                targetRole = 0
            )
        ),
        listOf(
            Blueprint(
                chapter = Chapter.TRADE_AND_SWAP,
                skill = SkillTag.SUBSTITUTION,
                ratios = listOf(1, 3, 1),
                relations = listOf(Relation(3, 0, 1, 1), Relation(1, 2, 1, 0)),
                leftRoles = listOf(1),
                targetRole = 2
            ),
            Blueprint(
                chapter = Chapter.TRADE_AND_SWAP,
                skill = SkillTag.SUBSTITUTION,
                ratios = listOf(1, 5, 1),
                relations = listOf(Relation(5, 0, 1, 1), Relation(1, 2, 1, 0)),
                leftRoles = listOf(1),
                targetRole = 2
            )
        ),
        listOf(
            Blueprint(
                chapter = Chapter.BRASS_WEIGHTS,
                skill = SkillTag.KNOWN_WEIGHTS,
                ratios = listOf(1, 3),
                relations = listOf(Relation(1, 1, 3, 0)),
                leftRoles = listOf(1, 1),
                targetRole = 0,
                brassRole = 0
            ),
            Blueprint(
                chapter = Chapter.BRASS_WEIGHTS,
                skill = SkillTag.KNOWN_WEIGHTS,
                ratios = listOf(1, 2),
                relations = listOf(Relation(1, 1, 2, 0)),
                leftRoles = listOf(1, 1),
                targetRole = 0,
                brassRole = 0
            )
        ),
        listOf(
            Blueprint(
                chapter = Chapter.TWO_STEPS_AHEAD,
                skill = SkillTag.MULTI_STEP,
                ratios = listOf(1, 2, 3),
                relations = listOf(Relation(2, 0, 1, 1), Relation(3, 0, 1, 2)),
                leftRoles = listOf(1, 2),
                targetRole = 0
            ),
            Blueprint(
                chapter = Chapter.TWO_STEPS_AHEAD,
                skill = SkillTag.MULTI_STEP,
                ratios = listOf(1, 2, 4),
                relations = listOf(Relation(2, 0, 1, 1), Relation(4, 0, 1, 2)),
                leftRoles = listOf(1, 2),
                targetRole = 0
            )
        ),
        listOf(
            Blueprint(
                chapter = Chapter.MASTER_BENCH,
                skill = SkillTag.MULTI_STEP,
                ratios = listOf(1, 2, 3, 6),
                relations = listOf(
                    Relation(2, 0, 1, 1),
                    Relation(3, 0, 1, 2),
                    Relation(2, 2, 1, 3)
                ),
                leftRoles = listOf(3),
                targetRole = 0
            ),
            Blueprint(
                chapter = Chapter.MASTER_BENCH,
                skill = SkillTag.MULTI_STEP,
                ratios = listOf(1, 2, 5, 5),
                relations = listOf(
                    Relation(2, 0, 1, 1),
                    Relation(5, 0, 1, 2),
                    Relation(1, 2, 1, 3)
                ),
                leftRoles = listOf(3),
                targetRole = 0
            )
        )
    )

    private fun buildPuzzles(): List<Puzzle> {
        val result = mutableListOf<Puzzle>()
        bench.forEachIndexed { chapterIndex, variants ->
            for (slot in 0 until GameConfig.PUZZLES_PER_CHAPTER) {
                val blueprint = variants[slot % variants.size]
                val unit = 1 + (slot / variants.size) % 3
                val rotation = slot % shapeOrder.size
                result.add(
                    assemble(
                        blueprint = blueprint,
                        index = chapterIndex * GameConfig.PUZZLES_PER_CHAPTER + slot,
                        unit = unit,
                        rotation = rotation
                    )
                )
            }
        }
        return result
    }

    private fun assemble(blueprint: Blueprint, index: Int, unit: Int, rotation: Int): Puzzle {
        val shapes = rolesToShapes(blueprint, rotation)
        val weights = LinkedHashMap<ShapeType, Int>()
        blueprint.ratios.forEachIndexed { role, ratio ->
            weights[shapes[role]] = ratio * unit
        }
        val clues = blueprint.relations.map { relation ->
            ClueScale(
                left = List(relation.leftCount) { shapes[relation.leftRole] },
                right = List(relation.rightCount) { shapes[relation.rightRole] }
            )
        }
        val leftPan = blueprint.leftRoles.map { shapes[it] }
        val target = shapes[blueprint.targetRole]
        val leftSum = blueprint.leftRoles.sumOf { blueprint.ratios[it] * unit }
        val targetWeight = blueprint.ratios[blueprint.targetRole] * unit
        val answer = (leftSum / targetWeight).coerceIn(1, GameConfig.MAX_PER_PAN)
        val tray = buildList {
            add(target)
            shapes.forEach { shape -> if (!contains(shape)) add(shape) }
        }
        return Puzzle(
            index = index,
            chapter = blueprint.chapter,
            skill = blueprint.skill,
            weights = weights,
            clues = clues,
            leftPan = leftPan,
            trayShapes = tray,
            targetShape = target,
            optimalMoves = answer
        )
    }

    private fun rolesToShapes(blueprint: Blueprint, rotation: Int): List<ShapeType> {
        val shapes = mutableListOf<ShapeType>()
        var cursor = 0
        blueprint.ratios.indices.forEach { role ->
            if (role == blueprint.brassRole) {
                shapes.add(ShapeType.BRASS_WEIGHT)
            } else {
                shapes.add(shapeOrder[(rotation + cursor) % shapeOrder.size])
                cursor++
            }
        }
        return shapes
    }
}
