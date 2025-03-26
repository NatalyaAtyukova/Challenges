package com.challenges.data.local

import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty

object DatabaseInitializerRu {
    val initialChallenges = listOf(
        // Легкие разговорные челленджи
        Challenge(
            title = "Разговор с коллегой",
            description = "Начните непринужденную беседу с коллегой о прошедших выходных",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Беседа в кофейне",
            description = "Завяжите разговор с кем-нибудь, пока стоите в очереди за кофе",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Разговор о погоде",
            description = "Начните разговор о погоде и плавно перейдите к более интересной теме",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Обсуждение книг",
            description = "Спросите кого-нибудь о книге, которую они читают, или об их любимой книге",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Разговор о питомцах",
            description = "Начните разговор с кем-нибудь об их домашних животных",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Беседа о кино",
            description = "Обсудите с кем-нибудь недавно просмотренный фильм",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Разговор о еде",
            description = "Начните беседу о любимых блюдах или ресторанах",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Планы на выходные",
            description = "Спросите кого-нибудь о планах на выходные и поделитесь своими",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Истории о путешествиях",
            description = "Поделитесь историей о путешествии и расспросите об опыте других",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Разговор о хобби",
            description = "Обсудите увлечения с кем-нибудь и проявите искренний интерес",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        // Челленджи средней сложности
        Challenge(
            title = "Глубокая беседа",
            description = "Начните содержательный разговор о жизненных целях и стремлениях",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Культурный обмен",
            description = "Начните разговор о разных культурах и традициях",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Карьерный путь",
            description = "Обсудите с кем-нибудь выбор карьеры и профессиональное развитие",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Влияние технологий",
            description = "Поговорите о том, как технологии влияют на нашу жизнь",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Экологические проблемы",
            description = "Начните разговор об экологических проблемах и их решениях",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Влияние соцсетей",
            description = "Обсудите влияние социальных сетей на общество",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Система образования",
            description = "Поговорите об образовании и методах обучения",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Баланс работы и жизни",
            description = "Начните разговор о поддержании баланса между работой и личной жизнью",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Технологии будущего",
            description = "Обсудите прогнозы о развитии технологий в будущем",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Психическое здоровье",
            description = "Проведите вдумчивую беседу о психическом здоровье и благополучии",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        // Сложные разговорные челленджи
        Challenge(
            title = "Разрешение конфликтов",
            description = "Потренируйтесь разрешать разногласия через спокойное обсуждение",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Философская дискуссия",
            description = "Вступите в философскую дискуссию о существовании и предназначении",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Политическая дискуссия",
            description = "Проведите уважительную беседу о различных политических взглядах",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Этические дилеммы",
            description = "Обсудите сложные этические сценарии и моральный выбор",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Научная дискуссия",
            description = "Вступите в обсуждение спорных научных тем",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Религиозный диалог",
            description = "Проведите уважительную беседу о различных религиозных верованиях",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Экономические системы",
            description = "Обсудите различные экономические системы и их влияние",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Социальная справедливость",
            description = "Поговорите о социальной справедливости и равенстве",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Глобальные вызовы",
            description = "Обсудите основные глобальные проблемы и возможные решения",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Будущее человечества",
            description = "Проведите глубокую дискуссию о будущем человеческой цивилизации",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        // Дополнительные легкие челленджи
        Challenge(
            title = "Музыкальные предпочтения",
            description = "Обсудите любимые музыкальные жанры и исполнителей",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Разговор о спорте",
            description = "Начните беседу о спорте или фитнес-активностях",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Местные события",
            description = "Обсудите предстоящие местные мероприятия или активности",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Сериалы",
            description = "Поговорите о любимых сериалах и телешоу",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Опыт покупок",
            description = "Начните разговор о предпочтениях в шоппинге",
            category = "CONVERSATION",
            difficulty = "EASY"
        ),
        // Дополнительные челленджи средней сложности
        Challenge(
            title = "Цифровая приватность",
            description = "Обсудите вопросы цифровой приватности и защиты данных",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Городское развитие",
            description = "Поговорите о развитии города и городском планировании",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Тренды здоровья",
            description = "Обсудите современные тренды в здоровье и велнесе",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Удаленная работа",
            description = "Поделитесь опытом удаленной работы и цифрового кочевничества",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Творческое искусство",
            description = "Поговорите о различных формах искусства",
            category = "CONVERSATION",
            difficulty = "MEDIUM"
        ),
        // Дополнительные сложные челленджи
        Challenge(
            title = "Этика ИИ",
            description = "Обсудите этические последствия развития искусственного интеллекта",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Культурная идентичность",
            description = "Исследуйте темы культурной идентичности и принадлежности",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Поколенческий разрыв",
            description = "Обсудите различия между поколениями и взаимопонимание",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Медиаграмотность",
            description = "Поговорите о критическом мышлении и потреблении медиа",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Социальные изменения",
            description = "Исследуйте механизмы социальных изменений и активизма",
            category = "CONVERSATION",
            difficulty = "HARD"
        ),
        // Челленджи VIDEO - Легкие
        Challenge(
            title = "Ежедневный влог",
            description = "Запишите короткое видео о своем дне и поделитесь впечатлениями",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Обучающее видео",
            description = "Создайте простое обучающее видео о том, что вы хорошо умеете делать",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Обзор книги",
            description = "Запишите видео-обзор недавно прочитанной книги",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Утренняя рутина",
            description = "Снимите свою утреннюю рутину и добавьте комментарии",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Демонстрация хобби",
            description = "Создайте видео, показывающее одно из ваших увлечений",
            category = "VIDEO",
            difficulty = "EASY"
        ),
        // Челленджи VIDEO - Средние
        Challenge(
            title = "Видео-интервью",
            description = "Создайте видео в формате интервью с другом или членом семьи",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Время историй",
            description = "Расскажите увлекательную историю на камеру с правильным темпом и структурой",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Обзор продукта",
            description = "Создайте подробный видео-обзор продукта, которым вы пользуетесь",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Трэвел-влог",
            description = "Создайте видео в стиле путешествия о вашем районе",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Образовательный контент",
            description = "Создайте обучающее видео, объясняющее концепцию, которую вы хорошо понимаете",
            category = "VIDEO",
            difficulty = "MEDIUM"
        ),
        // Челленджи VIDEO - Сложные
        Challenge(
            title = "Документальный стиль",
            description = "Создайте короткое видео в документальном стиле на интересную тему",
            category = "VIDEO",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Новостной репортаж",
            description = "Создайте новостной репортаж о местном событии или проблеме",
            category = "VIDEO",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Короткометражный фильм",
            description = "Спланируйте и создайте короткое видео с четким сюжетом",
            category = "VIDEO",
            difficulty = "HARD"
        ),
        // Челленджи PUBLIC_SPEAKING - Легкие
        Challenge(
            title = "Самопрезентация",
            description = "Сделайте минутную презентацию о себе перед небольшой группой",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Краткий пересказ книги",
            description = "Представьте краткое содержание вашей любимой книги друзьям",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Дневные новости",
            description = "Поделитесь основными новостями дня с коллегами",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Презентация хобби",
            description = "Сделайте короткий доклад о своем увлечении перед небольшой группой",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Объяснение рецепта",
            description = "Объясните, как приготовить ваше любимое блюдо, перед аудиторией",
            category = "PUBLIC",
            difficulty = "EASY"
        ),
        // Челленджи PUBLIC_SPEAKING - Средние
        Challenge(
            title = "Тематическая презентация",
            description = "Сделайте 5-минутную презентацию на тему, которая вас увлекает",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Импровизированная речь",
            description = "Произнесите 2-минутную речь на случайную тему с минимальной подготовкой",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Питч проекта",
            description = "Представьте идею проекта группе, объясняя его преимущества",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Образовательный доклад",
            description = "Сделайте образовательную презентацию о том, что вы хорошо знаете",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Рассказ истории",
            description = "Расскажите увлекательную историю группе, используя правильные приемы повествования",
            category = "PUBLIC",
            difficulty = "MEDIUM"
        ),
        // Челленджи PUBLIC_SPEAKING - Сложные
        Challenge(
            title = "Участие в дебатах",
            description = "Поучаствуйте в структурированных дебатах на сложную тему",
            category = "PUBLIC",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Техническая презентация",
            description = "Сделайте подробную техническую презентацию для подготовленной аудитории",
            category = "PUBLIC",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Мотивационная речь",
            description = "Произнесите мотивационную речь, чтобы вдохновить и зарядить аудиторию",
            category = "PUBLIC",
            difficulty = "HARD"
        ),
        // Челленджи DAILY - Легкие
        Challenge(
            title = "Утренняя благодарность",
            description = "Поделитесь тремя вещами, за которые вы благодарны сегодня",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Цель дня",
            description = "Поставьте и поделитесь одной главной целью на день",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Прогноз погоды",
            description = "Сделайте краткий прогноз погоды на день",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Обзор дня",
            description = "Поделитесь кратким обзором главных моментов вашего дня",
            category = "DAILY",
            difficulty = "EASY"
        ),
        Challenge(
            title = "Проверка настроения",
            description = "Выразите, как вы себя чувствуете сегодня и почему",
            category = "DAILY",
            difficulty = "EASY"
        ),
        // Челленджи DAILY - Средние
        Challenge(
            title = "Ежедневное обучение",
            description = "Поделитесь чем-то новым, что вы узнали сегодня",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Размышление о вызове",
            description = "Обсудите проблему, с которой вы столкнулись сегодня, и как вы с ней справились",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Достижение дня",
            description = "Поделитесь достижением или прогрессом, которого вы достигли сегодня",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Отчет о продуктивности",
            description = "Проанализируйте свою продуктивность и что вы успели сделать за день",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        Challenge(
            title = "Ежедневное вдохновение",
            description = "Поделитесь тем, что вдохновило вас сегодня",
            category = "DAILY",
            difficulty = "MEDIUM"
        ),
        // Челленджи DAILY - Сложные
        Challenge(
            title = "Анализ дня",
            description = "Предоставьте подробный анализ ваших дневных активностей и извлеченных уроков",
            category = "DAILY",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Влияние на день",
            description = "Обсудите, как ваши действия сегодня повлияли на других",
            category = "DAILY",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Планирование будущего",
            description = "Поделитесь планами на завтра и как они связаны с вашими долгосрочными целями",
            category = "DAILY",
            difficulty = "HARD"
        ),
        Challenge(
            title = "Начните разговор с незнакомцем",
            description = "Подойдите к незнакомому человеку и начните осмысленный разговор",
            category = "CONVERSATION",
            difficulty = "EASY",
            points = 10
        ),
        Challenge(
            title = "Запишите 1-минутное видео",
            description = "Создайте короткое видео на любую интересную вам тему",
            category = "VIDEO",
            difficulty = "MEDIUM",
            points = 20
        )
    )
} 