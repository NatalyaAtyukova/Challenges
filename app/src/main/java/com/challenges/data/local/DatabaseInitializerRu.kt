package com.challenges.data.local

import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty

object DatabaseInitializerRu {
    val initialChallenges = listOf(
        Challenge(
            title = "Расскажи историю",
            description = "Расскажи интересную историю из своей жизни на камеру",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Публичное выступление",
            description = "Выступи с короткой речью перед группой людей",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Начни разговор",
            description = "Начни разговор с незнакомым человеком на интересную тему",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Ежедневная практика",
            description = "Запиши короткое видео о том, как прошел твой день",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Импровизация",
            description = "Придумай и расскажи историю на случайную тему без подготовки",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Видеоблог",
            description = "Создай короткий видеоблог на интересующую тебя тему",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Дебаты",
            description = "Поучаствуй в дебатах на актуальную тему",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Утренний влог",
            description = "Запиши свое утро и расскажи о своих планах на день",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        )
    )
} 