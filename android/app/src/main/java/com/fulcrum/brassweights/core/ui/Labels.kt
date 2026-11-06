package com.fulcrum.brassweights.core.ui

import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.domain.model.Chapter
import com.fulcrum.brassweights.domain.model.GradeLevel
import com.fulcrum.brassweights.domain.model.ShapeType
import com.fulcrum.brassweights.domain.model.SkillTag

fun shapeDrawable(shape: ShapeType): Int = when (shape) {
    ShapeType.TRIANGLE -> R.drawable.sprite_1
    ShapeType.SQUARE -> R.drawable.sprite_2
    ShapeType.CIRCLE -> R.drawable.sprite_3
    ShapeType.HEXAGON -> R.drawable.sprite_4
    ShapeType.BRASS_WEIGHT -> R.drawable.sprite_5
}

fun shapeNameRes(shape: ShapeType, count: Int): Int = when (shape) {
    ShapeType.TRIANGLE -> if (count == 1) R.string.shape_triangle_one else R.string.shape_triangle_many
    ShapeType.SQUARE -> if (count == 1) R.string.shape_square_one else R.string.shape_square_many
    ShapeType.CIRCLE -> if (count == 1) R.string.shape_circle_one else R.string.shape_circle_many
    ShapeType.HEXAGON -> if (count == 1) R.string.shape_hexagon_one else R.string.shape_hexagon_many
    ShapeType.BRASS_WEIGHT -> if (count == 1) R.string.shape_weight_one else R.string.shape_weight_many
}

fun shapeLetterRes(shape: ShapeType): Int = when (shape) {
    ShapeType.TRIANGLE -> R.string.letter_triangle
    ShapeType.SQUARE -> R.string.letter_square
    ShapeType.CIRCLE -> R.string.letter_circle
    ShapeType.HEXAGON -> R.string.letter_hexagon
    ShapeType.BRASS_WEIGHT -> R.string.letter_weight
}

fun chapterNameRes(chapter: Chapter): Int = when (chapter) {
    Chapter.FIRST_BALANCE -> R.string.chapter_first_balance
    Chapter.TWIN_SHAPES -> R.string.chapter_twin_shapes
    Chapter.DOUBLE_UP -> R.string.chapter_double_up
    Chapter.TRADE_AND_SWAP -> R.string.chapter_trade_and_swap
    Chapter.BRASS_WEIGHTS -> R.string.chapter_brass_weights
    Chapter.TWO_STEPS_AHEAD -> R.string.chapter_two_steps
    Chapter.MASTER_BENCH -> R.string.chapter_master_bench
}

fun skillNameRes(skill: SkillTag): Int = when (skill) {
    SkillTag.SUBSTITUTION -> R.string.skill_substitution
    SkillTag.DOUBLING -> R.string.skill_doubling
    SkillTag.MULTI_STEP -> R.string.skill_multi_step
    SkillTag.KNOWN_WEIGHTS -> R.string.skill_known_weights
}

fun gradeNameRes(grade: GradeLevel): Int = when (grade) {
    GradeLevel.EARLY -> R.string.grade_early
    GradeLevel.MIDDLE -> R.string.grade_middle
    GradeLevel.ADVANCED -> R.string.grade_advanced
}
