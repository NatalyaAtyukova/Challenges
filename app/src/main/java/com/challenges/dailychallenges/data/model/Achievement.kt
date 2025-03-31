package com.challenges.dailychallenges.data.model

data class Achievement(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val iconName: String = "",
    val points: Int = 0,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val condition: String = AchievementType.CREATED_CHALLENGES.value,
    val category: String? = null,
    val difficulty: String? = null,
    val threshold: Int = 0,
    val progress: Int = 0
) {
    constructor() : this(
        id = "",
        title = "",
        description = "",
        iconName = "",
        points = 0,
        isUnlocked = false,
        unlockedAt = null,
        condition = AchievementType.CREATED_CHALLENGES.value,
        category = null,
        difficulty = null,
        threshold = 0,
        progress = 0
    )
} 