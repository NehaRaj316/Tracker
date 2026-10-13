package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.DayPlan
import com.example.data.model.MockTest
import com.example.data.model.ScheduleTemplate
import com.example.data.model.TimeSlot
import com.example.data.model.TopicRevision
import com.example.data.model.RevisionStatus
import com.example.data.repository.PlannerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class PlannerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlannerRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PlannerRepository(db.dayPlanDao(), db.mockTestDao(), db.topicRevisionDao())
        viewModelScope.launch {
            repository.ensureDataInitialized()
        }
    }

    val dayPlans: StateFlow<List<DayPlan>> = repository.allDayPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mockTests: StateFlow<List<MockTest>> = repository.allMockTests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attemptedMockTests: StateFlow<List<MockTest>> = repository.attemptedMockTests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedDaysCount: StateFlow<Int> = repository.completedDaysCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val attemptedMocksCount: StateFlow<Int> = repository.attemptedMocksCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCompletedBlocks: StateFlow<Int> = combine(dayPlans) { plansArray ->
        plansArray[0].sumOf { it.completedTaskCount() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val overallStudyProgress: StateFlow<Float> = combine(totalCompletedBlocks) { blocks ->
        (blocks[0].toFloat() / 300f).coerceIn(0f, 1f)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val topicRevisions: StateFlow<List<TopicRevision>> = repository.allTopicRevisions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val masteredTopicsCount: StateFlow<Int> = topicRevisions.map { list ->
        list.count { it.status == "Mastered" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val needsReviewTopicsCount: StateFlow<Int> = topicRevisions.map { list ->
        list.count { it.status == "Needs Review" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val notStartedTopicsCount: StateFlow<Int> = topicRevisions.map { list ->
        list.count { it.status == "Not Started" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _selectedDayNumber = MutableStateFlow(1)
    val selectedDayNumber: StateFlow<Int> = _selectedDayNumber.asStateFlow()

    val currentDayPlan: StateFlow<DayPlan?> = combine(dayPlans, selectedDayNumber) { plans, selectedNum ->
        plans.find { it.dayNumber == selectedNum }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Timer State
    private val _activeSlot = MutableStateFlow<TimeSlot?>(null)
    val activeSlot: StateFlow<TimeSlot?> = _activeSlot.asStateFlow()

    private val _pomodoroMode = MutableStateFlow(com.example.data.model.PomodoroMode.POMODORO_25)
    val pomodoroMode: StateFlow<com.example.data.model.PomodoroMode> = _pomodoroMode.asStateFlow()

    private val _pomodoroCycles = MutableStateFlow(0)
    val pomodoroCycles: StateFlow<Int> = _pomodoroCycles.asStateFlow()

    private val _timerSecondsRemaining = MutableStateFlow(0)
    val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

    private val _timerTotalSeconds = MutableStateFlow(0)
    val timerTotalSeconds: StateFlow<Int> = _timerTotalSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    // Find current active slot based on 10 AM to 10 PM timetable and actual clock
    fun getCurrentScheduleSlot(): TimeSlot? {
        val calendar = Calendar.getInstance()
        val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        return ScheduleTemplate.dailySlots.find { slot ->
            currentMinutes >= slot.startMinuteOfDay && currentMinutes < slot.endMinuteOfDay
        }
    }

    fun selectDay(dayNumber: Int) {
        _selectedDayNumber.value = dayNumber
    }

    fun toggleTask(dayNumber: Int, taskKey: String) {
        val currentPlan = dayPlans.value.find { it.dayNumber == dayNumber } ?: return
        val updated = when (taskKey) {
            "quant" -> currentPlan.copy(quantCompleted = !currentPlan.quantCompleted)
            "reasoning" -> currentPlan.copy(reasoningCompleted = !currentPlan.reasoningCompleted)
            "lunch" -> currentPlan.copy(lunchCompleted = !currentPlan.lunchCompleted)
            "mock" -> currentPlan.copy(mockCompleted = !currentPlan.mockCompleted)
            "mock_analysis" -> currentPlan.copy(mockAnalysisCompleted = !currentPlan.mockAnalysisCompleted)
            "english" -> currentPlan.copy(englishCompleted = !currentPlan.englishCompleted)
            "walking" -> currentPlan.copy(walkingCompleted = !currentPlan.walkingCompleted)
            "ga" -> currentPlan.copy(gaCompleted = !currentPlan.gaCompleted)
            "dinner" -> currentPlan.copy(dinnerCompleted = !currentPlan.dinnerCompleted)
            "revision" -> currentPlan.copy(revisionCompleted = !currentPlan.revisionCompleted)
            else -> currentPlan
        }
        val isAllCompleted = updated.completedTaskCount() == updated.totalTaskCount()
        val finalPlan = updated.copy(isCompleted = isAllCompleted)
        viewModelScope.launch {
            repository.updateDayPlan(finalPlan)
        }
    }

    fun markStudyBlockCompleted(dayNumber: Int, slotType: com.example.data.model.SlotType, isCompleted: Boolean) {
        val currentPlan = dayPlans.value.find { it.dayNumber == dayNumber } ?: return
        val updated = when (slotType) {
            com.example.data.model.SlotType.STUDY_QUANT -> currentPlan.copy(quantCompleted = isCompleted)
            com.example.data.model.SlotType.STUDY_REASONING -> currentPlan.copy(reasoningCompleted = isCompleted)
            com.example.data.model.SlotType.STUDY_ENGLISH -> currentPlan.copy(englishCompleted = isCompleted)
            com.example.data.model.SlotType.STUDY_GA -> currentPlan.copy(gaCompleted = isCompleted)
            com.example.data.model.SlotType.MOCK_TEST -> currentPlan.copy(mockCompleted = isCompleted)
            com.example.data.model.SlotType.MOCK_ANALYSIS -> currentPlan.copy(mockAnalysisCompleted = isCompleted)
            com.example.data.model.SlotType.LUNCH -> currentPlan.copy(lunchCompleted = isCompleted)
            com.example.data.model.SlotType.DINNER -> currentPlan.copy(dinnerCompleted = isCompleted)
            com.example.data.model.SlotType.WALKING -> currentPlan.copy(walkingCompleted = isCompleted)
            com.example.data.model.SlotType.REVISION -> currentPlan.copy(revisionCompleted = isCompleted)
            com.example.data.model.SlotType.BREAK -> currentPlan
        }
        val isAllCompleted = updated.completedTaskCount() == updated.totalTaskCount()
        val finalPlan = updated.copy(isCompleted = isAllCompleted)
        viewModelScope.launch {
            repository.updateDayPlan(finalPlan)
            if (slotType == com.example.data.model.SlotType.MOCK_TEST) {
                repository.setMockAttemptStatus(dayNumber, isCompleted)
            }
        }
    }

    fun markMockTestCompleted(testNumber: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.setMockAttemptStatus(testNumber, isCompleted)
            val dayPlan = dayPlans.value.find { it.dayNumber == testNumber }
            if (dayPlan != null && dayPlan.mockCompleted != isCompleted) {
                val updated = dayPlan.copy(mockCompleted = isCompleted)
                val isAllDone = updated.completedTaskCount() == updated.totalTaskCount()
                repository.updateDayPlan(updated.copy(isCompleted = isAllDone))
            }
        }
    }

    fun markAllDayBlocksCompleted(dayNumber: Int, isCompleted: Boolean = true) {
        val currentPlan = dayPlans.value.find { it.dayNumber == dayNumber } ?: return
        val updated = currentPlan.copy(
            quantCompleted = isCompleted,
            reasoningCompleted = isCompleted,
            lunchCompleted = isCompleted,
            mockCompleted = isCompleted,
            mockAnalysisCompleted = isCompleted,
            englishCompleted = isCompleted,
            walkingCompleted = isCompleted,
            gaCompleted = isCompleted,
            dinnerCompleted = isCompleted,
            revisionCompleted = isCompleted,
            isCompleted = isCompleted
        )
        viewModelScope.launch {
            repository.updateDayPlan(updated)
            repository.setMockAttemptStatus(dayNumber, isCompleted)
        }
    }

    fun resetDayProgress(dayNumber: Int) {
        markAllDayBlocksCompleted(dayNumber, false)
    }

    fun saveNotes(dayNumber: Int, notes: String) {
        viewModelScope.launch {
            repository.updateNotes(dayNumber, notes)
        }
    }

    fun startTimerForSlot(slot: TimeSlot) {
        startPomodoroForSlot(slot, com.example.data.model.PomodoroMode.POMODORO_25)
    }

    fun startPomodoroForSlot(
        slot: TimeSlot,
        mode: com.example.data.model.PomodoroMode = com.example.data.model.PomodoroMode.POMODORO_25
    ) {
        timerJob?.cancel()
        _activeSlot.value = slot
        _pomodoroMode.value = mode

        val minutes = if (mode == com.example.data.model.PomodoroMode.FULL_SESSION) {
            slot.durationMinutes
        } else {
            mode.durationMinutes
        }

        val totalSec = minutes * 60
        _timerTotalSeconds.value = totalSec
        _timerSecondsRemaining.value = totalSec
        _isTimerRunning.value = true

        launchTimerCountdown()
    }

    fun setPomodoroMode(mode: com.example.data.model.PomodoroMode) {
        val slot = _activeSlot.value ?: return
        startPomodoroForSlot(slot, mode)
    }

    fun adjustTimer(deltaMinutes: Int) {
        val deltaSeconds = deltaMinutes * 60
        val newRemaining = (_timerSecondsRemaining.value + deltaSeconds).coerceAtLeast(10)
        _timerSecondsRemaining.value = newRemaining
        if (newRemaining > _timerTotalSeconds.value) {
            _timerTotalSeconds.value = newRemaining
        }
    }

    fun skipToNextPomodoroPhase() {
        val currentSlot = _activeSlot.value ?: return
        val currentMode = _pomodoroMode.value
        val nextMode = when (currentMode) {
            com.example.data.model.PomodoroMode.POMODORO_25,
            com.example.data.model.PomodoroMode.DEEP_FOCUS_50,
            com.example.data.model.PomodoroMode.FULL_SESSION -> {
                // If 4 focus cycles completed, take long break; otherwise short break
                if ((_pomodoroCycles.value + 1) % 4 == 0) {
                    com.example.data.model.PomodoroMode.LONG_BREAK
                } else {
                    com.example.data.model.PomodoroMode.SHORT_BREAK
                }
            }
            com.example.data.model.PomodoroMode.SHORT_BREAK,
            com.example.data.model.PomodoroMode.LONG_BREAK -> {
                com.example.data.model.PomodoroMode.POMODORO_25
            }
        }
        startPomodoroForSlot(currentSlot, nextMode)
    }

    fun dismissActiveTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _activeSlot.value = null
    }

    private fun launchTimerCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSecondsRemaining.value > 0 && _isTimerRunning.value) {
                delay(1000)
                if (_isTimerRunning.value) {
                    _timerSecondsRemaining.value -= 1
                }
            }
            if (_timerSecondsRemaining.value == 0) {
                _isTimerRunning.value = false
                if (!_pomodoroMode.value.isBreak) {
                    _pomodoroCycles.value += 1
                }
                vibrateOnTimerEnd()
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
    }

    fun resumeTimer() {
        if (_timerSecondsRemaining.value > 0) {
            _isTimerRunning.value = true
            launchTimerCountdown()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _timerSecondsRemaining.value = _timerTotalSeconds.value
    }

    private fun vibrateOnTimerEnd() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 500, 200, 500), -1)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 500, 200, 500), -1)
            }
        } catch (_: Exception) {
            // Non-critical vibration fallback
        }
    }

    fun saveMockTest(
        testNumber: Int,
        provider: String,
        quantScore: Float,
        reasoningScore: Float,
        englishScore: Float,
        gaScore: Float,
        attempted: Int,
        correct: Int,
        wrong: Int,
        timeMinutes: Int,
        errorAnalysis: String,
        weakTopics: String,
        dateAttemptedMillis: Long? = null
    ) {
        val total = quantScore + reasoningScore + englishScore + gaScore
        val accuracy = if (attempted > 0) (correct.toFloat() / attempted.toFloat()) * 100f else 0f
        val existing = mockTests.value.find { it.testNumber == testNumber }
        val updated = (existing ?: MockTest(testNumber = testNumber, title = "SSC CGL Tier-1 Full Mock #$testNumber")).copy(
            provider = provider,
            isAttempted = true,
            quantScore = quantScore,
            reasoningScore = reasoningScore,
            englishScore = englishScore,
            gaScore = gaScore,
            totalScore = total,
            attemptedQuestions = attempted,
            correctAnswers = correct,
            wrongAnswers = wrong,
            accuracyPercent = accuracy,
            timeTakenMinutes = timeMinutes,
            dateAttemptedMillis = dateAttemptedMillis ?: existing?.dateAttemptedMillis ?: System.currentTimeMillis(),
            errorAnalysis = errorAnalysis,
            weakTopics = weakTopics
        )
        viewModelScope.launch {
            repository.saveMockTest(updated)
            // Also mark corresponding day's mock task as completed
            val dayPlan = dayPlans.value.find { it.dayNumber == testNumber }
            if (dayPlan != null && !dayPlan.mockCompleted) {
                toggleTask(testNumber, "mock")
            }
        }
    }

    fun resetMockTest(testNumber: Int) {
        viewModelScope.launch {
            repository.resetMockTest(testNumber)
            val dayPlan = dayPlans.value.find { it.dayNumber == testNumber }
            if (dayPlan != null && dayPlan.mockCompleted) {
                toggleTask(testNumber, "mock")
            }
        }
    }

    fun updateTopicRevisionStatus(topicId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateTopicRevisionStatus(topicId, newStatus)
        }
    }

    fun updateTopicRevisionNotes(topicId: Long, notes: String) {
        viewModelScope.launch {
            repository.updateTopicRevisionNotes(topicId, notes)
        }
    }

    fun addTopicRevision(
        dayNumber: Int,
        subject: String,
        topicTitle: String,
        status: String = "Not Started",
        notes: String = ""
    ) {
        viewModelScope.launch {
            repository.saveTopicRevision(
                TopicRevision(
                    dayNumber = dayNumber,
                    subject = subject,
                    topicTitle = topicTitle,
                    status = status,
                    notes = notes,
                    lastRevisedTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteTopicRevision(topic: TopicRevision) {
        viewModelScope.launch {
            repository.deleteTopicRevision(topic)
        }
    }
}
