package com.challenges.dailychallenges.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.challenges.dailychallenges.R
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.ui.components.AddChallengeDialog
import com.challenges.dailychallenges.ui.components.AppTopBar
import com.challenges.dailychallenges.ui.components.BottomNavigation
import com.challenges.dailychallenges.ui.components.ChallengeItem
import com.challenges.dailychallenges.ui.components.EditChallengeDialog
import com.challenges.dailychallenges.ui.theme.NeonBlue
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController

enum class ChallengeTab {
    MY_CHALLENGES,
    COMMUNITY
}

@Composable
fun MyChallengesScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    onNavigateToAuth: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val challenges by viewModel.challenges.collectAsState()
    
    var selectedTab by remember { mutableStateOf(ChallengeTab.MY_CHALLENGES) }
    var showAddDialog by remember { mutableStateOf(false) }
    var currentEditChallenge by remember { mutableStateOf<Challenge?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddSystemChallengeDialog by remember { mutableStateOf(false) }
    var showAddedConfirmation by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.my_challenges),
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.syncChallenges()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.sync_challenges),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onNavigateToAuth) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                navController = navController
            )
        },
        floatingActionButton = {
            if (selectedTab == ChallengeTab.MY_CHALLENGES) {
                Column(horizontalAlignment = Alignment.End) {
                    // Кнопка добавления системного челленджа
                    FloatingActionButton(
                        onClick = { showAddSystemChallengeDialog = true },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(Icons.Default.Public, contentDescription = "Добавить системный челлендж")
                    }
                    
                    // Кнопка создания своего челленджа
                    FloatingActionButton(
                        onClick = { showAddDialog = true },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_challenge))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs for My Challenges / Community
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == ChallengeTab.MY_CHALLENGES,
                    onClick = { selectedTab = ChallengeTab.MY_CHALLENGES },
                    text = { Text("Мои Челленджи") }
                )
                Tab(
                    selected = selectedTab == ChallengeTab.COMMUNITY,
                    onClick = { selectedTab = ChallengeTab.COMMUNITY },
                    text = { Text("Сообщество") }
                )
            }
            
            when (selectedTab) {
                ChallengeTab.MY_CHALLENGES -> {
                    if (viewModel.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else if (viewModel.error != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.error ?: stringResource(R.string.unknown_error),
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        val userChallenges = challenges.filter { it.isCustom }
                        val personalChallenges = userChallenges.filter { it.category != "Сообщество" && !it.title.startsWith("ADDED:") }
                        val joinedCommunityChallenges = userChallenges.filter { it.category == "Сообщество" }
                        val addedChallenges = userChallenges.filter { !it.title.startsWith("ADDED:") && it.category != "Сообщество" && !personalChallenges.contains(it) }
                        
                        if (userChallenges.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_custom_challenges),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (personalChallenges.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Мои челленджи",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                    
                                    items(personalChallenges) { challenge ->
                                        ChallengeItem(
                                            challenge = challenge,
                                            onClick = {
                                                currentEditChallenge = challenge
                                                showEditDialog = true
                                            },
                                            onCompletionToggle = { /* Personal challenges can't be marked as completed */ },
                                            allowCompletion = false
                                        )
                                    }
                                }
                                
                                if (joinedCommunityChallenges.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Присоединенные челленджи",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                        )
                                    }
                                    
                                    items(joinedCommunityChallenges) { challenge ->
                                        ChallengeItem(
                                            challenge = challenge,
                                            onClick = { /* Cannot edit community challenges */ },
                                            onCompletionToggle = { isCompleted ->
                                                viewModel.updateChallengeCompletion(challenge, isCompleted)
                                            },
                                            isEditable = false,
                                            allowCompletion = true
                                        )
                                    }
                                }
                                
                                if (addedChallenges.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Добавленные челленджи",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                        )
                                    }
                                    
                                    items(addedChallenges) { challenge ->
                                        ChallengeItem(
                                            challenge = challenge,
                                            onClick = { /* Cannot edit added challenges */ },
                                            onCompletionToggle = { isCompleted ->
                                                viewModel.updateChallengeCompletion(challenge, isCompleted)
                                            },
                                            isEditable = false,
                                            allowCompletion = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                ChallengeTab.COMMUNITY -> {
                    // Community challenges
                    if (viewModel.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Real community challenges list
                            items(getCommunityChalllenges()) { challengeData ->
                                CommunityItem(
                                    title = challengeData.title,
                                    description = challengeData.description,
                                    author = challengeData.author,
                                    likes = challengeData.likes,
                                    verified = challengeData.verified,
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.createCustomChallenge(
                                                title = challengeData.title,
                                                description = challengeData.description,
                                                category = "Сообщество",
                                                points = (challengeData.likes / 100) + 1
                                            )
                                        }
                                        // Show confirmation through snackbar or toast here if needed
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Success feedback
    if (showAddedConfirmation) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            showAddedConfirmation = false
        }
        
        androidx.compose.material3.Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                androidx.compose.material3.TextButton(
                    onClick = { showAddedConfirmation = false }
                ) {
                    Text("ОК")
                }
            }
        ) {
            Text("Челлендж успешно добавлен в ваш список!")
        }
    }
    
    // Диалоги
    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description, category, points ->
                viewModel.createCustomChallenge(title, description, category, points)
                showAddDialog = false
            }
        )
    }
    
    if (showAddSystemChallengeDialog) {
        SystemChallengesDialog(
            onDismiss = { showAddSystemChallengeDialog = false },
            onAddChallenge = { challenge ->
                viewModel.createCustomChallenge(
                    title = challenge.title,
                    description = challenge.description,
                    category = challenge.category,
                    points = challenge.points,
                    isFromSystem = true
                )
                showAddedConfirmation = true
            },
            challenges = challenges.filter { !it.isCustom },
            viewModel = viewModel
        )
    }
    
    if (showEditDialog && currentEditChallenge != null) {
        EditChallengeDialog(
            challenge = currentEditChallenge!!,
            onDismiss = { 
                showEditDialog = false
                currentEditChallenge = null
            },
            onConfirm = { title, description, category, points ->
                viewModel.editChallenge(currentEditChallenge!!, title, description, category, points)
                showEditDialog = false
                currentEditChallenge = null
            },
            onDelete = {
                viewModel.deleteChallenge(currentEditChallenge!!)
                showEditDialog = false
                currentEditChallenge = null
            }
        )
    }
}

@Composable
fun CommunityItem(
    title: String,
    description: String,
    author: String,
    likes: Int,
    verified: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (verified) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = NeonBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "От: $author",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Likes",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$likes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            androidx.compose.material3.Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Присоединиться")
            }
        }
    }
}

data class CommunityChallengeData(
    val title: String,
    val description: String,
    val author: String,
    val likes: Int,
    val verified: Boolean
)

fun getCommunityChalllenges(): List<CommunityChallengeData> {
    return listOf(
        CommunityChallengeData(
            title = "Запиши видео на тему твоего дня",
            description = "Создай 3-минутное видео о том, как прошел твой день, и поделись интересными моментами",
            author = "VideoCreator777",
            likes = 342,
            verified = true
        ),
        CommunityChallengeData(
            title = "Нарисуй что-то без отрыва руки",
            description = "Нарисуй связный рисунок, не отрывая карандаша или ручки от бумаги",
            author = "ArtistDaily",
            likes = 289,
            verified = true
        ),
        CommunityChallengeData(
            title = "Сделай подкаст с другом",
            description = "Запиши 15-минутный разговор в формате подкаста на интересующую вас тему",
            author = "PodcastMaster",
            likes = 176,
            verified = true
        ),
        CommunityChallengeData(
            title = "Напиши стихотворение о природе",
            description = "Создай небольшое стихотворение, вдохновляясь окружающей природой",
            author = "PoetryLover",
            likes = 123,
            verified = false
        ),
        CommunityChallengeData(
            title = "Проведи день без соцсетей",
            description = "Вызов на целый день не заходить ни в одну социальную сеть, вместо этого заняться чем-то полезным",
            author = "DigitalDetoxExpert",
            likes = 567,
            verified = true
        ),
        CommunityChallengeData(
            title = "Научи кого-то новому навыку",
            description = "Выбери навык, которым ты владеешь, и научи этому другого человека за 30 минут",
            author = "MentorMaster",
            likes = 321,
            verified = true
        ),
        CommunityChallengeData(
            title = "Сделай 100 отжиманий за день",
            description = "Выполни 100 отжиманий в течение дня (можно разбить на несколько подходов)",
            author = "FitnessCoach",
            likes = 432,
            verified = true
        ),
        CommunityChallengeData(
            title = "Приготовь блюдо из новой кухни",
            description = "Выбери рецепт из кухни, которую ты никогда не пробовал готовить, и приготовь новое блюдо",
            author = "ChefCooking",
            likes = 254,
            verified = false
        ),
        CommunityChallengeData(
            title = "Прочитай книгу за неделю",
            description = "Выбери книгу среднего размера и прочитай её полностью за 7 дней",
            author = "BookWorm",
            likes = 198,
            verified = true
        ),
        CommunityChallengeData(
            title = "Медитируй 10 минут ежедневно",
            description = "Практикуй 10-минутную медитацию каждый день в течение недели",
            author = "MindfulnessTeacher",
            likes = 365,
            verified = true
        ),
        CommunityChallengeData(
            title = "Изучи 20 новых слов на иностранном языке",
            description = "Выучи и используй в контексте 20 новых слов на любом иностранном языке",
            author = "LanguageLearner",
            likes = 241,
            verified = true
        ),
        CommunityChallengeData(
            title = "Нарисуй автопортрет",
            description = "Создай автопортрет в любой технике рисования",
            author = "ArtistPro",
            likes = 156,
            verified = false
        ),
        CommunityChallengeData(
            title = "Напиши письмо себе в будущее",
            description = "Составь письмо себе через 5 лет с ожиданиями, надеждами и советами",
            author = "FutureThinker",
            likes = 287,
            verified = true
        ),
        CommunityChallengeData(
            title = "Сделай 10 000 шагов за день",
            description = "Пройди минимум 10 000 шагов в течение одного дня",
            author = "WalkingChampion",
            likes = 429,
            verified = true
        ),
        CommunityChallengeData(
            title = "Научись базовому жонглированию",
            description = "Освой жонглирование тремя предметами за неделю тренировок",
            author = "CircusSkills",
            likes = 189,
            verified = false
        ),
        CommunityChallengeData(
            title = "Сними таймлапс заката",
            description = "Создай красивое видео заката в формате таймлапс",
            author = "TimeLapseCreator",
            likes = 312,
            verified = true
        ),
        CommunityChallengeData(
            title = "Веди дневник благодарности неделю",
            description = "Каждый день записывай 3 вещи, за которые ты благодарен",
            author = "GratitudeGuru",
            likes = 476,
            verified = true
        ),
        CommunityChallengeData(
            title = "Приберись в цифровом пространстве",
            description = "Удали ненужные файлы, организуй папки и почисти электронную почту",
            author = "DigitalOrganizer",
            likes = 267,
            verified = true
        ),
        CommunityChallengeData(
            title = "Создай плейлист из 20 новых треков",
            description = "Составь плейлист из песен, которые ты раньше не слушал",
            author = "MusicExplorer",
            likes = 183,
            verified = false
        ),
        CommunityChallengeData(
            title = "Проведи день без жалоб",
            description = "Попробуй провести целый день без единой жалобы на что-либо",
            author = "PositivityCoach",
            likes = 435,
            verified = true
        ),
        CommunityChallengeData(
            title = "Выучи базовый танцевальный шаг",
            description = "Освой один базовый шаг из любого танцевального стиля",
            author = "DanceMaster",
            likes = 278,
            verified = true
        ),
        CommunityChallengeData(
            title = "Составь бюджет на месяц",
            description = "Создай детальный план доходов и расходов на следующий месяц",
            author = "FinanceGuru",
            likes = 342,
            verified = true
        ),
        CommunityChallengeData(
            title = "Напиши рассказ из 500 слов",
            description = "Создай короткий рассказ ровно из 500 слов на любую тему",
            author = "StoryWriter",
            likes = 209,
            verified = false
        ),
        CommunityChallengeData(
            title = "Посети местный музей или выставку",
            description = "Сходи в музей или на выставку в своем городе, о которой раньше не знал",
            author = "CultureExplorer",
            likes = 187,
            verified = true
        ),
        CommunityChallengeData(
            title = "Создай решение экологической проблемы",
            description = "Придумай и реализуй небольшое решение для локальной экологической проблемы",
            author = "EcoWarrior",
            likes = 394,
            verified = true
        ),
        CommunityChallengeData(
            title = "Запиши подкаст о своем хобби",
            description = "Создай 20-минутный аудиоподкаст, рассказывая о своем любимом хобби",
            author = "HobbyEnthusiast",
            likes = 165,
            verified = false
        ),
        CommunityChallengeData(
            title = "Сделай фотосессию обычных предметов",
            description = "Создай серию художественных фотографий из повседневных предметов",
            author = "PhotoArtist",
            likes = 276,
            verified = true
        ),
        CommunityChallengeData(
            title = "Проведи встречу в новом формате",
            description = "Организуй рабочую или дружескую встречу в необычном формате",
            author = "MeetingInnovator",
            likes = 198,
            verified = true
        ),
        CommunityChallengeData(
            title = "Выполни 30-дневный челлендж по зарядке",
            description = "Делай ежедневную утреннюю зарядку в течение 30 дней без пропусков",
            author = "FitnessLife",
            likes = 467,
            verified = true
        ),
        CommunityChallengeData(
            title = "Научись основам программирования",
            description = "Пройди вводный онлайн-курс по любому языку программирования",
            author = "CodeMaster",
            likes = 345,
            verified = false
        ),
        CommunityChallengeData(
            title = "Создай красивую композицию из растений",
            description = "Составь флорариум или террариум из растений своими руками",
            author = "PlantLover",
            likes = 287,
            verified = true
        ),
        CommunityChallengeData(
            title = "Проведи день без гаджетов",
            description = "Проведи 24 часа без использования телефона, компьютера и других гаджетов",
            author = "DigitalDetoxPro",
            likes = 512,
            verified = true
        ),
        CommunityChallengeData(
            title = "Изучи искусство оригами",
            description = "Научись складывать 5 разных фигурок в технике оригами",
            author = "OrigamiMaster",
            likes = 176,
            verified = false
        ),
        CommunityChallengeData(
            title = "Пройди 100 км за неделю",
            description = "Преодолей дистанцию в 100 километров за 7 дней (бег, ходьба, велосипед)",
            author = "EnduranceAthlete",
            likes = 398,
            verified = true
        ),
        CommunityChallengeData(
            title = "Напиши стих каждый день в течение недели",
            description = "Создавай по одному стихотворению ежедневно на протяжении 7 дней",
            author = "PoemWriter",
            likes = 243,
            verified = true
        ),
        CommunityChallengeData(
            title = "Изучи искусство каллиграфии",
            description = "Освой основы каллиграфического письма за 10 дней ежедневной практики",
            author = "CalligraphyArtist",
            likes = 276,
            verified = true
        ),
        CommunityChallengeData(
            title = "Приготовь 7 блюд из разных стран",
            description = "За неделю приготовь по одному блюду из кухни разных стран мира",
            author = "GlobalChef",
            likes = 389,
            verified = false
        ),
        CommunityChallengeData(
            title = "Изучи основы фотографии",
            description = "Изучи базовые принципы фотографии и сделай серию снимков, применяя новые знания",
            author = "PhotographyMentor",
            likes = 324,
            verified = true
        ),
        CommunityChallengeData(
            title = "Проведи день абсолютной тишины",
            description = "Проведи 24 часа в полном молчании, не произнося ни слова",
            author = "SilencePractitioner",
            likes = 256,
            verified = true
        ),
        CommunityChallengeData(
            title = "Создай 3D модель любого предмета",
            description = "Используя доступное ПО, создай трехмерную модель любого предмета",
            author = "3DModelingPro",
            likes = 217,
            verified = false
        ),
        CommunityChallengeData(
            title = "Проведи неделю ранних подъемов",
            description = "Вставай в 5:30 утра каждый день в течение недели",
            author = "EarlyRiser",
            likes = 421,
            verified = true
        ),
        CommunityChallengeData(
            title = "Изучи 10 созвездий на ночном небе",
            description = "Найди и научись распознавать 10 различных созвездий на ночном небе",
            author = "StarGazer",
            likes = 298,
            verified = true
        ),
        CommunityChallengeData(
            title = "Сделай скетчи 7 разных объектов",
            description = "Нарисуй быстрые скетчи семи различных предметов из своего окружения",
            author = "SketchArtist",
            likes = 186,
            verified = false
        ),
        CommunityChallengeData(
            title = "Собери природную коллекцию",
            description = "Создай коллекцию из природных материалов (листьев, камней, ракушек и т.д.)",
            author = "NatureCollector",
            likes = 267,
            verified = true
        ),
        CommunityChallengeData(
            title = "Изучи основы нового музыкального инструмента",
            description = "Освой базовые навыки игры на музыкальном инструменте, который ты раньше не пробовал",
            author = "MusicLearner",
            likes = 352,
            verified = true
        ),
        CommunityChallengeData(
            title = "Создай модный образ из старой одежды",
            description = "Преобрази старую одежду в стильный современный наряд",
            author = "FashionUpcycler",
            likes = 275,
            verified = false
        ),
        CommunityChallengeData(
            title = "Проведи день без сахара",
            description = "Откажись от продуктов с добавленным сахаром на 24 часа",
            author = "HealthyEater",
            likes = 387,
            verified = true
        ),
        CommunityChallengeData(
            title = "Создай вирусное видео для соцсетей",
            description = "Придумай и сними короткое видео, которое может стать вирусным",
            author = "ContentCreatorPro",
            likes = 456,
            verified = true
        ),
        CommunityChallengeData(
            title = "Сделай 5 добрых дел незнакомцам",
            description = "Выполни 5 случайных актов доброты для людей, которых не знаешь",
            author = "KindnessAmbassador",
            likes = 512,
            verified = true
        ),
        CommunityChallengeData(
            title = "Попробуй 7 дней веганства",
            description = "Питайся исключительно растительной пищей в течение недели",
            author = "PlantBasedExplorer",
            likes = 347,
            verified = false
        )
    )
}

@Composable
fun SystemChallengesDialog(
    onDismiss: () -> Unit,
    onAddChallenge: (Challenge) -> Unit,
    challenges: List<Challenge>,
    viewModel: MainViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить челлендж из общих") },
        text = { 
            Column {
                androidx.compose.material3.OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Поиск") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    val filteredChallenges = challenges.filter { 
                        it.title.contains(searchQuery, ignoreCase = true) || 
                        it.description.contains(searchQuery, ignoreCase = true) 
                    }
                    
                    items(filteredChallenges) { challenge ->
                        SystemChallengeItem(
                            challenge = challenge,
                            onClick = { onAddChallenge(challenge) }
                        )
                    }
                    
                    if (filteredChallenges.isEmpty()) {
                        item {
                            Text(
                                text = "Нет доступных челленджей",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = onDismiss
            ) {
                Text("Закрыть")
            }
        }
    )
}

@Composable
fun SystemChallengeItem(
    challenge: Challenge,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Категория: ${challenge.category}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Text(
                    text = "${challenge.points} очков",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            androidx.compose.material3.Button(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Добавить к своим")
            }
        }
    }
} 