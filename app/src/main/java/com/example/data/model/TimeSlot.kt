package com.example.data.model

enum class SlotType {
    STUDY_QUANT,
    BREAK,
    STUDY_REASONING,
    LUNCH,
    MOCK_TEST,
    MOCK_ANALYSIS,
    STUDY_ENGLISH,
    WALKING,
    STUDY_GA,
    DINNER,
    REVISION
}

data class TimeSlot(
    val id: String,
    val type: SlotType,
    val startTime: String,
    val endTime: String,
    val startMinuteOfDay: Int, // e.g. 10:00 = 600
    val endMinuteOfDay: Int,   // e.g. 11:30 = 690
    val durationMinutes: Int,
    val title: String,
    val subtitle: String,
    val subjectTag: String,
    val isBreakOrMealOrWalk: Boolean
)

object ScheduleTemplate {
    val dailySlots: List<TimeSlot> = listOf(
        TimeSlot(
            id = "slot_quant",
            type = SlotType.STUDY_QUANT,
            startTime = "10:00 AM",
            endTime = "11:30 AM",
            startMinuteOfDay = 10 * 60,
            endMinuteOfDay = 11 * 60 + 30,
            durationMinutes = 90,
            title = "Quant Mastery & Problem Solving",
            subtitle = "Core arithmetic & advance math problem sets, speed techniques",
            subjectTag = "Quantitative Aptitude",
            isBreakOrMealOrWalk = false
        ),
        TimeSlot(
            id = "slot_break1",
            type = SlotType.BREAK,
            startTime = "11:30 AM",
            endTime = "11:45 AM",
            startMinuteOfDay = 11 * 60 + 30,
            endMinuteOfDay = 11 * 60 + 45,
            durationMinutes = 15,
            title = "Hydration & Stretch Break",
            subtitle = "Step away from desk, eye relaxation & deep breathing",
            subjectTag = "Break",
            isBreakOrMealOrWalk = true
        ),
        TimeSlot(
            id = "slot_reasoning",
            type = SlotType.STUDY_REASONING,
            startTime = "11:45 AM",
            endTime = "01:15 PM",
            startMinuteOfDay = 11 * 60 + 45,
            endMinuteOfDay = 13 * 60 + 15,
            durationMinutes = 90,
            title = "Reasoning Speed Drills",
            subtitle = "Logical puzzles, series, syllogisms & non-verbal figures",
            subjectTag = "Reasoning",
            isBreakOrMealOrWalk = false
        ),
        TimeSlot(
            id = "slot_break2",
            type = SlotType.BREAK,
            startTime = "01:15 PM",
            endTime = "01:30 PM",
            startMinuteOfDay = 13 * 60 + 15,
            endMinuteOfDay = 13 * 60 + 30,
            durationMinutes = 15,
            title = "Pre-Lunch Wind-down",
            subtitle = "Organize morning notes & prepare for afternoon mock",
            subjectTag = "Break",
            isBreakOrMealOrWalk = true
        ),
        TimeSlot(
            id = "slot_lunch",
            type = SlotType.LUNCH,
            startTime = "01:30 PM",
            endTime = "02:30 PM",
            startMinuteOfDay = 13 * 60 + 30,
            endMinuteOfDay = 14 * 60 + 30,
            durationMinutes = 60,
            title = "Lunch Break (1 Hour)",
            subtitle = "Nutritious meal & rest to recharge brain energy",
            subjectTag = "Meal",
            isBreakOrMealOrWalk = true
        ),
        TimeSlot(
            id = "slot_mock",
            type = SlotType.MOCK_TEST,
            startTime = "02:30 PM",
            endTime = "03:30 PM",
            startMinuteOfDay = 14 * 60 + 30,
            endMinuteOfDay = 15 * 60 + 30,
            durationMinutes = 60,
            title = "Full-Length Mock Test (CBT Mode)",
            subtitle = "Strict 60-min timer, 100 questions (200 marks) simulation",
            subjectTag = "Mock Test",
            isBreakOrMealOrWalk = false
        ),
        TimeSlot(
            id = "slot_mock_analysis",
            type = SlotType.MOCK_ANALYSIS,
            startTime = "03:30 PM",
            endTime = "04:30 PM",
            startMinuteOfDay = 15 * 60 + 30,
            endMinuteOfDay = 16 * 60 + 30,
            durationMinutes = 60,
            title = "In-Depth Mock Analysis & Error Book",
            subtitle = "Categorize silly mistakes, unattempted Qs & solution methods",
            subjectTag = "Analysis",
            isBreakOrMealOrWalk = false
        ),
        TimeSlot(
            id = "slot_break3",
            type = SlotType.BREAK,
            startTime = "04:30 PM",
            endTime = "04:45 PM",
            startMinuteOfDay = 16 * 60 + 30,
            endMinuteOfDay = 16 * 60 + 45,
            durationMinutes = 15,
            title = "Afternoon Tea / Coffee Break",
            subtitle = "Quick mental reset before language session",
            subjectTag = "Break",
            isBreakOrMealOrWalk = true
        ),
        TimeSlot(
            id = "slot_english",
            type = SlotType.STUDY_ENGLISH,
            startTime = "04:45 PM",
            endTime = "05:45 PM",
            startMinuteOfDay = 16 * 60 + 45,
            endMinuteOfDay = 17 * 60 + 45,
            durationMinutes = 60,
            title = "English Language & Comprehension",
            subtitle = "Grammar rules, Cloze test, Idioms, OWS & reading practice",
            subjectTag = "English",
            isBreakOrMealOrWalk = false
        ),
        TimeSlot(
            id = "slot_walking",
            type = SlotType.WALKING,
            startTime = "05:45 PM",
            endTime = "06:45 PM",
            startMinuteOfDay = 17 * 60 + 45,
            endMinuteOfDay = 18 * 60 + 45,
            durationMinutes = 60,
            title = "Evening Walking (1 Hour)",
            subtitle = "Brisk outdoor walking for blood circulation & stamina",
            subjectTag = "Health & Walking",
            isBreakOrMealOrWalk = true
        ),
        TimeSlot(
            id = "slot_ga",
            type = SlotType.STUDY_GA,
            startTime = "06:45 PM",
            endTime = "08:00 PM",
            startMinuteOfDay = 18 * 60 + 45,
            endMinuteOfDay = 20 * 60,
            durationMinutes = 75,
            title = "General Awareness & Current Affairs",
            subtitle = "Polity, History, Geography, Science & Monthly CA quizzes",
            subjectTag = "General Awareness",
            isBreakOrMealOrWalk = false
        ),
        TimeSlot(
            id = "slot_dinner",
            type = SlotType.DINNER,
            startTime = "08:00 PM",
            endTime = "09:00 PM",
            startMinuteOfDay = 20 * 60,
            endMinuteOfDay = 21 * 60,
            durationMinutes = 60,
            title = "Dinner Break (1 Hour)",
            subtitle = "Relaxed meal & family/unwind time",
            subjectTag = "Meal",
            isBreakOrMealOrWalk = true
        ),
        TimeSlot(
            id = "slot_revision",
            type = SlotType.REVISION,
            startTime = "09:00 PM",
            endTime = "10:00 PM",
            startMinuteOfDay = 21 * 60,
            endMinuteOfDay = 22 * 60,
            durationMinutes = 60,
            title = "Rigorous Nightly Revision & Formula Book",
            subtitle = "Review all math formulas, today's mistakes & vocab flashcards",
            subjectTag = "Rigorous Revision",
            isBreakOrMealOrWalk = false
        )
    )
}
