package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MockTest
import kotlinx.coroutines.flow.Flow

@Dao
interface MockTestDao {
    @Query("SELECT * FROM mock_tests ORDER BY testNumber ASC")
    fun getAllMockTests(): Flow<List<MockTest>>

    @Query("SELECT * FROM mock_tests WHERE testNumber = :testNumber")
    fun getMockTest(testNumber: Int): Flow<MockTest?>

    @Query("SELECT * FROM mock_tests WHERE isAttempted = 1 ORDER BY testNumber ASC")
    fun getAttemptedMockTests(): Flow<List<MockTest>>

    @Query("SELECT COUNT(*) FROM mock_tests WHERE isAttempted = 1")
    fun getAttemptedCount(): Flow<Int>

    @Query("SELECT AVG(totalScore) FROM mock_tests WHERE isAttempted = 1")
    fun getAverageScore(): Flow<Float?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mockTests: List<MockTest>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockTest(mockTest: MockTest)

    @Update
    suspend fun updateMockTest(mockTest: MockTest)

    @Query("UPDATE mock_tests SET isAttempted = :isAttempted WHERE testNumber = :testNumber")
    suspend fun updateAttemptStatus(testNumber: Int, isAttempted: Boolean)

    @Query("UPDATE mock_tests SET isAttempted = 0, quantScore = 0, reasoningScore = 0, englishScore = 0, gaScore = 0, totalScore = 0, attemptedQuestions = 0, correctAnswers = 0, wrongAnswers = 0, accuracyPercent = 0, dateAttemptedMillis = null, errorAnalysis = '', weakTopics = '' WHERE testNumber = :testNumber")
    suspend fun resetMockTest(testNumber: Int)

    @Query("SELECT COUNT(*) FROM mock_tests")
    suspend fun getCount(): Int
}
