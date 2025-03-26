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
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Coffee Shop Chat",
            description = "Strike up a conversation with someone while waiting in line at a coffee shop",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Weather Talk",
            description = "Start a conversation about the weather and transition it into a more interesting topic",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Book Discussion",
            description = "Ask someone about the book they're reading or their favorite book",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Pet Conversation",
            description = "Start a conversation with someone about their pets",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Movie Chat",
            description = "Discuss a recent movie you've watched with someone",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Food Discussion",
            description = "Start a conversation about favorite foods or restaurants",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Weekend Plans",
            description = "Ask someone about their weekend plans and share yours",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Travel Stories",
            description = "Share a travel story and ask about others' experiences",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Hobby Talk",
            description = "Discuss hobbies with someone and show genuine interest",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        // Medium Conversation Challenges
        Challenge(
            title = "Deep Discussion",
            description = "Initiate a meaningful conversation about life goals and aspirations",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Cultural Exchange",
            description = "Start a conversation about different cultures and traditions",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Career Path",
            description = "Discuss career choices and professional development with someone",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Technology Impact",
            description = "Engage in a discussion about how technology affects our lives",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Environmental Issues",
            description = "Start a conversation about environmental challenges and solutions",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Social Media Influence",
            description = "Discuss the impact of social media on society",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Educational System",
            description = "Engage in a discussion about education and learning methods",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Work-Life Balance",
            description = "Start a conversation about maintaining work-life balance",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Future Technology",
            description = "Discuss predictions about future technological developments",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Mental Health",
            description = "Have a thoughtful discussion about mental health and well-being",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        // Hard Conversation Challenges
        Challenge(
            title = "Conflict Resolution",
            description = "Practice resolving a disagreement through calm discussion",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Philosophical Debate",
            description = "Engage in a philosophical discussion about existence and purpose",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Political Discussion",
            description = "Have a respectful conversation about different political views",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Ethical Dilemmas",
            description = "Discuss complex ethical scenarios and moral choices",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Scientific Debate",
            description = "Engage in a discussion about controversial scientific topics",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Religious Dialogue",
            description = "Have a respectful conversation about different religious beliefs",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Economic Systems",
            description = "Discuss different economic systems and their impacts",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Social Justice",
            description = "Engage in a discussion about social justice and equality",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Global Challenges",
            description = "Discuss major global challenges and potential solutions",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Future of Humanity",
            description = "Have a deep discussion about the future of human civilization",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        // Additional Easy Challenges
        Challenge(
            title = "Music Preferences",
            description = "Discuss favorite music genres and artists",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Sports Talk",
            description = "Start a conversation about sports or fitness activities",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Local Events",
            description = "Discuss upcoming local events or activities",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "TV Shows",
            description = "Talk about favorite TV shows and series",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Shopping Experience",
            description = "Start a conversation about shopping preferences",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        // Additional Medium Challenges
        Challenge(
            title = "Digital Privacy",
            description = "Discuss concerns about digital privacy and data protection",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Urban Development",
            description = "Talk about city development and urban planning",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Health Trends",
            description = "Discuss modern health trends and wellness practices",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Remote Work",
            description = "Share experiences about remote work and digital nomad lifestyle",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Creative Arts",
            description = "Engage in a discussion about various forms of art",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        // Additional Hard Challenges
        Challenge(
            title = "AI Ethics",
            description = "Discuss ethical implications of artificial intelligence",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Cultural Identity",
            description = "Explore topics of cultural identity and belonging",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Generation Gap",
            description = "Discuss intergenerational differences and understanding",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Media Literacy",
            description = "Discuss critical thinking and media consumption",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Social Change",
            description = "Explore mechanisms of social change and activism",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        // VIDEO Challenges - Easy
        Challenge(
            title = "Daily Vlog",
            description = "Record a short video about your day and share your experiences",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Tutorial Video",
            description = "Create a simple how-to video about something you're good at",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Book Review",
            description = "Record a video review of a book you've recently read",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Morning Routine",
            description = "Film your morning routine and add commentary",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Hobby Showcase",
            description = "Create a video showcasing one of your hobbies",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        // VIDEO Challenges - Medium
        Challenge(
            title = "Interview Style Video",
            description = "Create an interview-style video with a friend or family member",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Story Time",
            description = "Tell an engaging story on camera with proper pacing and structure",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Product Review",
            description = "Create a detailed review video of a product you use",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Travel Vlog",
            description = "Create a travel-style video about your local area",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Educational Content",
            description = "Create an educational video explaining a concept you understand well",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        // VIDEO Challenges - Hard
        Challenge(
            title = "Documentary Style",
            description = "Create a short documentary-style video about an interesting topic",
            category = "VIDEO",
            difficulty = "HARD"
        ),
        Challenge(
            title = "News Report",
            description = "Create a news-style report about a local event or issue",
            category = "VIDEO",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Scripted Short Film",
            description = "Plan and create a short scripted video with a clear story",
            category = "VIDEO",
            difficulty = "HARD"
        ),
        // PUBLIC_SPEAKING Challenges - Easy
        Challenge(
            title = "Self Introduction",
            description = "Give a 1-minute introduction about yourself to a small group",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Book Summary",
            description = "Present a brief summary of your favorite book to friends",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Daily News",
            description = "Share today's main news headlines with colleagues",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Hobby Presentation",
            description = "Give a short talk about your hobby to a small group",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Recipe Explanation",
            description = "Explain how to cook your favorite dish to an audience",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        // PUBLIC_SPEAKING Challenges - Medium
        Challenge(
            title = "Topic Presentation",
            description = "Give a 5-minute presentation on a topic you're passionate about",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Impromptu Speech",
            description = "Give a 2-minute speech on a random topic with minimal preparation",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Project Pitch",
            description = "Present a project idea to a group, explaining its benefits",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Educational Talk",
            description = "Give an educational presentation about something you know well",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Story Telling",
            description = "Tell an engaging story to a group using proper narrative techniques",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        // PUBLIC_SPEAKING Challenges - Hard
        Challenge(
            title = "Debate Participation",
            description = "Participate in a structured debate on a challenging topic",
            category = "PUBLIC",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Technical Presentation",
            description = "Give a detailed technical presentation to an informed audience",
            category = "PUBLIC",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Motivational Speech",
            description = "Deliver a motivational speech to inspire and energize an audience",
            category = "PUBLIC",
            difficulty = "HARD"
        ),
        // DAILY Challenges - Easy
        Challenge(
            title = "Morning Gratitude",
            description = "Share three things you're grateful for today",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Daily Goal",
            description = "Set and share one main goal for the day",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Weather Report",
            description = "Give a brief weather report for the day",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Daily Review",
            description = "Share a brief review of your day's highlights",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Mood Check",
            description = "Express how you're feeling today and why",
            category = "DAILY",
            difficulty = "EASY"
        ),
        // DAILY Challenges - Medium
        Challenge(
            title = "Daily Learning",
            description = "Share something new you learned today",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Challenge Reflection",
            description = "Discuss a challenge you faced today and how you handled it",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Daily Achievement",
            description = "Share an achievement or progress you made today",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Productivity Report",
            description = "Review your productivity and what you accomplished today",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Daily Inspiration",
            description = "Share something that inspired you today",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        // DAILY Challenges - Hard
        Challenge(
            title = "Day Analysis",
            description = "Provide a detailed analysis of your day's activities and lessons learned",
            category = "DAILY",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Daily Impact",
            description = "Discuss how your actions today impacted others",
            category = "DAILY",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Future Planning",
            description = "Share your plans for tomorrow and how they connect to your long-term goals",
            category = "DAILY",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Start a conversation with a stranger",
            description = "Approach someone you don't know and start a meaningful conversation",
            category = "CONVERSATION",
            difficulty = "EASY",
            points = 10
        ),
        Challenge(
            title = "Record a 1-minute video",
            description = "Create a short video about any topic you're passionate about",
            category = "VIDEO",
            difficulty = "MEDIUM",
            points = 20
        )
    )
} 