package com.challenges.dailychallenges.data.model

enum class AchievementType(val value: String) {
    CATEGORY_COMPLETED("CATEGORY_COMPLETED"),
    DIFFICULTY_COMPLETED("DIFFICULTY_COMPLETED"),
    CREATED_CHALLENGES("CREATED_CHALLENGES"),
    POINTS_LOW("POINTS_LOW"),
    POINTS_MEDIUM("POINTS_MEDIUM"),
    POINTS_HIGH("POINTS_HIGH");

    companion object {
        fun fromString(value: String): AchievementType {
            return values().find { it.value == value } ?: CREATED_CHALLENGES
        }
    }
} 