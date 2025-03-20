package com.challenges.data.local

import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty

object DatabaseInitializer {
    val initialChallenges = listOf(
        Challenge(
            title = "Tell a Story",
            description = "Tell an interesting story from your life on camera",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Public Speaking",
            description = "Give a short speech in front of a group of people",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Start a Conversation",
            description = "Start a conversation with a stranger on an interesting topic",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Daily Practice",
            description = "Record a short video about how your day went",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Improvisation",
            description = "Make up and tell a story on a random topic without preparation",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Video Blog",
            description = "Create a short video blog about a topic that interests you",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Debate",
            description = "Participate in a debate on a current topic",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Morning Vlog",
            description = "Record your morning and talk about your plans for the day",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        )
    )
} 