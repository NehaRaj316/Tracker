package com.example.data.repository

import com.example.data.db.DayPlanDao
import com.example.data.db.InitialData
import com.example.data.db.MockTestDao
import com.example.data.db.TopicRevisionDao
import com.example.data.model.DayPlan
import com.example.data.model.MockTest
import com.example.data.model.TopicRevision
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlannerRepository(
    private val dayPlanDao: DayPlanDao,
    private val mockTestDao: MockTestDao,
    private val topicRevisionDao: TopicRevisionDao
) {
    val allDayPlans: Flow<List<DayPlan>> = dayPlanDao.getAllDayPlans()
    val allMockTests: Flow<List<MockTest>> = mockTestDao.getAllMockTests()
    val attemptedMockTests: Flow<List<MockTest>> = mockTestDao.getAttemptedMockTests()
    val completedDaysCount: Flow<Int> = dayPlanDao.getCompletedDaysCount()
    val attemptedMocksCount: Flow<Int> = mockTestDao.getAttemptedCount()
    val averageMockScore: Flow<Float?> = mockTestDao.getAverageScore()
    val allTopicRevisions: Flow<List<TopicRevision>> = topicRevisionDao.getAllTopicRevisions()

    fun getDayPlan(dayNumber: Int): Flow<DayPlan?> = dayPlanDao.getDayPlanByNumber(dayNumber)

    fun getTopicRevisionsForDay(dayNumber: Int): Flow<List<TopicRevision>> =
        topicRevisionDao.getTopicRevisionsForDay(dayNumber)

    suspend fun updateDayPlan(dayPlan: DayPlan) = withContext(Dispatchers.IO) {
        dayPlanDao.updateDayPlan(dayPlan)
    }

    suspend fun updateNotes(dayNumber: Int, notes: String) = withContext(Dispatchers.IO) {
        dayPlanDao.updateNotes(dayNumber, notes)
    }

    suspend fun updateMockTest(mockTest: MockTest) = withContext(Dispatchers.IO) {
        mockTestDao.updateMockTest(mockTest)
    }

    suspend fun saveMockTest(mockTest: MockTest) = withContext(Dispatchers.IO) {
        mockTestDao.insertMockTest(mockTest)
    }

    suspend fun resetMockTest(testNumber: Int) = withContext(Dispatchers.IO) {
        mockTestDao.resetMockTest(testNumber)
    }

    suspend fun setMockAttemptStatus(testNumber: Int, isAttempted: Boolean) = withContext(Dispatchers.IO) {
        mockTestDao.updateAttemptStatus(testNumber, isAttempted)
    }

    suspend fun updateTopicRevisionStatus(id: Long, status: String) = withContext(Dispatchers.IO) {
        topicRevisionDao.updateStatus(id, status)
    }

    suspend fun updateTopicRevisionNotes(id: Long, notes: String) = withContext(Dispatchers.IO) {
        topicRevisionDao.updateNotes(id, notes)
    }

    suspend fun saveTopicRevision(topicRevision: TopicRevision) = withContext(Dispatchers.IO) {
        topicRevisionDao.insert(topicRevision)
    }

    suspend fun deleteTopicRevision(topicRevision: TopicRevision) = withContext(Dispatchers.IO) {
        topicRevisionDao.delete(topicRevision)
    }

    suspend fun ensureDataInitialized() = withContext(Dispatchers.IO) {
        val planCount = dayPlanDao.getCount()
        if (planCount == 0) {
            dayPlanDao.insertAll(InitialData.get31DayPlans())
        } else if (planCount < 31) {
            val missingDays = InitialData.get31DayPlans().filter { it.dayNumber > planCount }
            if (missingDays.isNotEmpty()) {
                dayPlanDao.insertAll(missingDays)
            }
        }
        val mockCount = mockTestDao.getCount()
        if (mockCount == 0) {
            mockTestDao.insertAll(InitialData.get31MockTests())
        } else if (mockCount < 31) {
            val missingMocks = InitialData.get31MockTests().filter { it.testNumber > mockCount }
            if (missingMocks.isNotEmpty()) {
                mockTestDao.insertAll(missingMocks)
            }
        }
        val topicCount = topicRevisionDao.getCount()
        if (topicCount == 0) {
            topicRevisionDao.insertAll(InitialData.getDefaultTopicRevisions())
        }
    }
}
