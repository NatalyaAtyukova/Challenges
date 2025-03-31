package com.challenges.dailychallenges.di

import android.content.Context
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.preferences.UserPreferences
import com.challenges.dailychallenges.data.repository.AchievementRepository
import com.challenges.dailychallenges.data.repository.CommunityRepository
import com.challenges.dailychallenges.data.repository.FirebaseRepository
import com.challenges.dailychallenges.data.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EnglishChallenges

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RussianChallenges

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FirebaseRepository {
        return FirebaseRepository(firestore, auth)
    }
    
    @Provides
    @Singleton
    fun provideUserProfileRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        auth: FirebaseAuth
    ): UserProfileRepository {
        return UserProfileRepository(firestore, storage, auth)
    }
    
    @Provides
    @Singleton
    fun provideCommunityRepository(firestore: FirebaseFirestore): CommunityRepository {
        return CommunityRepository(firestore)
    }
    
    @Provides
    @Singleton
    fun provideAchievementRepository(
        firestore: FirebaseFirestore,
        firebaseRepository: FirebaseRepository
    ): AchievementRepository {
        return AchievementRepository(firestore, firebaseRepository)
    }
    
    @Provides
    @Singleton
    @EnglishChallenges
    fun provideEnglishChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                title = "Small Talk with a Colleague",
                description = "Start a casual conversation with a colleague about their day",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            ),
            Challenge(
                title = "Coffee Shop Chat",
                description = "Order coffee and have a brief conversation with the barista",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            ),
            Challenge(
                title = "Weather Discussion",
                description = "Discuss the weather with someone",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            ),
            Challenge(
                title = "Book Recommendations",
                description = "Ask someone for book recommendations",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Pet Stories",
                description = "Share stories about pets with someone",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Movie Discussion",
                description = "Discuss a recent movie with someone",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Food Preferences",
                description = "Discuss favorite foods with someone",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Weekend Plans",
                description = "Ask someone about their weekend plans",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Travel Stories",
                description = "Share travel experiences with someone",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Hobby Discussion",
                description = "Discuss hobbies with someone",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Career Goals",
                description = "Discuss career aspirations with someone",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Life Philosophy",
                description = "Share life philosophy with someone",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Create a Video",
                description = "Create a short video on any topic",
                category = "VIDEO",
                difficulty = "MEDIUM",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Public Speaking",
                description = "Give a presentation to an audience",
                category = "PUBLIC",
                difficulty = "HARD",
                points = 150,
                isCustom = false
            ),
            Challenge(
                title = "Daily Task",
                description = "Complete a daily task",
                category = "DAILY",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            )
        )
    }

    @Provides
    @Singleton
    @RussianChallenges
    fun provideRussianChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                title = "Разговор с коллегой",
                description = "Начните непринужденную беседу с коллегой о его дне",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            ),
            Challenge(
                title = "Беседа в кофейне",
                description = "Закажите кофе и поговорите с бариста",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            ),
            Challenge(
                title = "Разговор о погоде",
                description = "Обсудите погоду с кем-нибудь",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            ),
            Challenge(
                title = "Обсуждение книг",
                description = "Попросите кого-нибудь порекомендовать книги",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Разговор о питомцах",
                description = "Поделитесь историями о домашних животных",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Беседа о кино",
                description = "Обсудите недавний фильм с кем-нибудь",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Разговор о еде",
                description = "Обсудите любимые блюда с кем-нибудь",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Планы на выходные",
                description = "Спросите кого-нибудь о планах на выходные",
                category = "CONVERSATION",
                difficulty = "MEDIUM",
                points = 75,
                isCustom = false
            ),
            Challenge(
                title = "Истории о путешествиях",
                description = "Поделитесь историями о путешествиях",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Разговор о хобби",
                description = "Обсудите хобби с кем-нибудь",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Карьерные цели",
                description = "Обсудите карьерные устремления с кем-нибудь",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Жизненная философия",
                description = "Поделитесь жизненной философией с кем-нибудь",
                category = "CONVERSATION",
                difficulty = "HARD",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Создание видео",
                description = "Создайте короткое видео на любую тему",
                category = "VIDEO",
                difficulty = "MEDIUM",
                points = 100,
                isCustom = false
            ),
            Challenge(
                title = "Публичное выступление",
                description = "Выступите с презентацией перед аудиторией",
                category = "PUBLIC",
                difficulty = "HARD",
                points = 150,
                isCustom = false
            ),
            Challenge(
                title = "Ежедневная задача",
                description = "Выполните ежедневную задачу",
                category = "DAILY",
                difficulty = "EASY",
                points = 50,
                isCustom = false
            )
        )
    }

    @Provides
    @Singleton
    fun provideInitializeDatabase(
        firebaseRepository: FirebaseRepository,
        @EnglishChallenges englishChallenges: List<Challenge>,
        @RussianChallenges russianChallenges: List<Challenge>
    ): Boolean {
        // Инициализация данных в Firebase
        CoroutineScope(Dispatchers.IO).launch {
            // Определяем челленджи на основе текущей локали
            val challenges = when (Locale.getDefault().language) {
                "ru" -> russianChallenges
                else -> englishChallenges
            }
            
            // Инициализируем челленджи в Firebase
            firebaseRepository.initializeDefaultChallenges(challenges)
        }
        
        return true
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
} 