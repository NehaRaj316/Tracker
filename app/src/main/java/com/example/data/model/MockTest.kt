package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mock_tests")
data class MockTest(
    @PrimaryKey
    val testNumber: Int, // 1 to 30
    val title: String,
    val provider: String = "Testbook", // Testbook, Oliveboard, Gradeup/BYJU's, Adda247, SuperCoaching, PYQ 2024
    val isAttempted: Boolean = false,
    val quantScore: Float = 0f,
    val reasoningScore: Float = 0f,
    val englishScore: Float = 0f,
    val gaScore: Float = 0f,
    val totalScore: Float = 0f,
    val totalQuestions: Int = 100,
    val attemptedQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val accuracyPercent: Float = 0f,
    val timeTakenMinutes: Int = 60,
    val dateAttemptedMillis: Long? = null,
    val errorAnalysis: String = "",
    val weakTopics: String = ""
)
