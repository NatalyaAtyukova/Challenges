package com.challenges.data.local

import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty

object DatabaseInitializer {
    val initialChallenges = listOf(
        // Easy Conversation Challenges
        Challenge(
            title = "Small Talk with a Colleague",
            description = "Start a casual conversation with a colleague about their weekend",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Coffee Shop Chat",
            description = "Strike up a conversation with someone while waiting in line at a coffee shop",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Weather Talk",
            description = "Start a conversation about the weather and transition it into a more interesting topic",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Book Discussion",
            description = "Ask someone about the book they're reading or their favorite book",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Pet Conversation",
            description = "Start a conversation with someone about their pets",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Movie Chat",
            description = "Discuss a recent movie you've watched with someone",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Food Discussion",
            description = "Start a conversation about favorite foods or restaurants",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Weekend Plans",
            description = "Ask someone about their weekend plans and share yours",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Travel Stories",
            description = "Share a travel story and ask about others' experiences",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Hobby Talk",
            description = "Discuss hobbies with someone and show genuine interest",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        // Medium Conversation Challenges
        Challenge(
            title = "Deep Discussion",
            description = "Initiate a meaningful conversation about life goals and aspirations",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Cultural Exchange",
            description = "Start a conversation about different cultures and traditions",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Career Path",
            description = "Discuss career choices and professional development with someone",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Technology Impact",
            description = "Engage in a discussion about how technology affects our lives",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Environmental Issues",
            description = "Start a conversation about environmental challenges and solutions",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Social Media Influence",
            description = "Discuss the impact of social media on society",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Educational System",
            description = "Engage in a discussion about education and learning methods",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Work-Life Balance",
            description = "Start a conversation about maintaining work-life balance",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Future Technology",
            description = "Discuss predictions about future technological developments",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Mental Health",
            description = "Have a thoughtful discussion about mental health and well-being",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        // Hard Conversation Challenges
        Challenge(
            title = "Conflict Resolution",
            description = "Practice resolving a disagreement through calm discussion",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Philosophical Debate",
            description = "Engage in a philosophical discussion about existence and purpose",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Political Discussion",
            description = "Have a respectful conversation about different political views",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Ethical Dilemmas",
            description = "Discuss complex ethical scenarios and moral choices",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Scientific Debate",
            description = "Engage in a discussion about controversial scientific topics",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Religious Dialogue",
            description = "Have a respectful conversation about different religious beliefs",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Economic Systems",
            description = "Discuss different economic systems and their impacts",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Social Justice",
            description = "Engage in a discussion about social justice and equality",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Global Challenges",
            description = "Discuss major global challenges and potential solutions",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Future of Humanity",
            description = "Have a deep discussion about the future of human civilization",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        // Additional Easy Challenges
        Challenge(
            title = "Music Preferences",
            description = "Discuss favorite music genres and artists",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Sports Talk",
            description = "Start a conversation about sports or fitness activities",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Local Events",
            description = "Discuss upcoming local events or activities",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "TV Shows",
            description = "Talk about favorite TV shows and series",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Shopping Experience",
            description = "Start a conversation about shopping preferences",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.EASY
        ),
        // Additional Medium Challenges
        Challenge(
            title = "Digital Privacy",
            description = "Discuss concerns about digital privacy and data protection",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Urban Development",
            description = "Talk about city development and urban planning",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Health Trends",
            description = "Discuss modern health trends and wellness practices",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Remote Work",
            description = "Share experiences about remote work and digital nomad lifestyle",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Creative Arts",
            description = "Engage in a discussion about various forms of art",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        // Additional Hard Challenges
        Challenge(
            title = "AI Ethics",
            description = "Discuss ethical implications of artificial intelligence",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Cultural Identity",
            description = "Explore topics of cultural identity and belonging",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Generation Gap",
            description = "Discuss intergenerational differences and understanding",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Media Literacy",
            description = "Discuss critical thinking and media consumption",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Social Change",
            description = "Explore mechanisms of social change and activism",
            category = ChallengeCategory.CONVERSATION,
            difficulty = ChallengeDifficulty.HARD
        ),
        // VIDEO Challenges - Easy
        Challenge(
            title = "Daily Vlog",
            description = "Record a short video about your day and share your experiences",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Tutorial Video",
            description = "Create a simple how-to video about something you're good at",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Book Review",
            description = "Record a video review of a book you've recently read",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Morning Routine",
            description = "Film your morning routine and add commentary",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Hobby Showcase",
            description = "Create a video showcasing one of your hobbies",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.EASY
        ),
        // VIDEO Challenges - Medium
        Challenge(
            title = "Interview Style Video",
            description = "Create an interview-style video with a friend or family member",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Story Time",
            description = "Tell an engaging story on camera with proper pacing and structure",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Product Review",
            description = "Create a detailed review video of a product you use",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Travel Vlog",
            description = "Create a travel-style video about your local area",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Educational Content",
            description = "Create an educational video explaining a concept you understand well",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        // VIDEO Challenges - Hard
        Challenge(
            title = "Documentary Style",
            description = "Create a short documentary-style video about an interesting topic",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "News Report",
            description = "Create a news-style report about a local event or issue",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Scripted Short Film",
            description = "Plan and create a short scripted video with a clear story",
            category = ChallengeCategory.VIDEO,
            difficulty = ChallengeDifficulty.HARD
        ),
        // PUBLIC_SPEAKING Challenges - Easy
        Challenge(
            title = "Self Introduction",
            description = "Give a 1-minute introduction about yourself to a small group",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Book Summary",
            description = "Present a brief summary of your favorite book to friends",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Daily News",
            description = "Share today's main news headlines with colleagues",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Hobby Presentation",
            description = "Give a short talk about your hobby to a small group",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Recipe Explanation",
            description = "Explain how to cook your favorite dish to an audience",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.EASY
        ),
        // PUBLIC_SPEAKING Challenges - Medium
        Challenge(
            title = "Topic Presentation",
            description = "Give a 5-minute presentation on a topic you're passionate about",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Impromptu Speech",
            description = "Give a 2-minute speech on a random topic with minimal preparation",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Project Pitch",
            description = "Present a project idea to a group, explaining its benefits",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Educational Talk",
            description = "Give an educational presentation about something you know well",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Story Telling",
            description = "Tell an engaging story to a group using proper narrative techniques",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        // PUBLIC_SPEAKING Challenges - Hard
        Challenge(
            title = "Debate Participation",
            description = "Participate in a structured debate on a challenging topic",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Technical Presentation",
            description = "Give a detailed technical presentation to an informed audience",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Motivational Speech",
            description = "Deliver a motivational speech to inspire and energize an audience",
            category = ChallengeCategory.PUBLIC,
            difficulty = ChallengeDifficulty.HARD
        ),
        // DAILY Challenges - Easy
        Challenge(
            title = "Morning Gratitude",
            description = "Share three things you're grateful for today",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Daily Goal",
            description = "Set and share one main goal for the day",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Weather Report",
            description = "Give a brief weather report for the day",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Daily Review",
            description = "Share a brief review of your day's highlights",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        Challenge(
            title = "Mood Check",
            description = "Express how you're feeling today and why",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.EASY
        ),
        // DAILY Challenges - Medium
        Challenge(
            title = "Daily Learning",
            description = "Share something new you learned today",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Challenge Reflection",
            description = "Discuss a challenge you faced today and how you handled it",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Daily Achievement",
            description = "Share an achievement or progress you made today",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Productivity Report",
            description = "Review your productivity and what you accomplished today",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        Challenge(
            title = "Daily Inspiration",
            description = "Share something that inspired you today",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.MEDIUM
        ),
        // DAILY Challenges - Hard
        Challenge(
            title = "Day Analysis",
            description = "Provide a detailed analysis of your day's activities and lessons learned",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Daily Impact",
            description = "Discuss how your actions today impacted others",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.HARD
        ),
        Challenge(
            title = "Future Planning",
            description = "Share your plans for tomorrow and how they connect to your long-term goals",
            category = ChallengeCategory.DAILY,
            difficulty = ChallengeDifficulty.HARD
        )
    )
} 