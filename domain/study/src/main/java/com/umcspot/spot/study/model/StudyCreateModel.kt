package com.umcspot.spot.study.model

import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme

data class StudyCreateModel(
    val name: String,
    val maxMembers: Int,
    val hasFee: Boolean,
    val amount: Int,
    val description: String,
    val categories: List<StudyTheme>,
    val styles: List<StudyStyle>,
    val regionCodes: List<String>
)

enum class StudyPersonality(
    val title: String,
    val leftLabel: String,
    val rightLabel: String,
    val option1: StudyStyle,
    val option2: StudyStyle
) {
    NETWORKING(
        "네트워킹",
        "네트워킹 중시",
        "목표/규율 중시",
        StudyStyle.NETWORKING,
        StudyStyle.GOAL_OR_RULE_ORIENTED
    ),
    DURATION("진행 기간", "단기 목표", "장기 목표", StudyStyle.SHORT_TERM, StudyStyle.LONG_TERM),
    TYPE(
        "진행 방식",
        "개인 학습 + 함께 토론형",
        "공동 학습 + 동시 진행형",
        StudyStyle.INDIVIDUAL_PLUS_DISCUSSION,
        StudyStyle.GROUP_PLUS_SIMULTANEOUS
    ),
    FOCUS("주요 활동", "학습형", "토론형", StudyStyle.LEARNING_BASED, StudyStyle.DISCUSSION_BASED),
    ATMOSPHERE(
        "분위기",
        "가볍게 + 유연하게",
        "규칙적인 + 계획적인",
        StudyStyle.LIGHT_AND_FLEXIBLE,
        StudyStyle.STRUCTURED_AND_PLANNED
    );


    fun getStyle(index: Int): StudyStyle {
        return if (index == 0) option1 else option2
    }

    companion object {
        val entriesList = entries.toList()
    }
}