package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_plans")
data class DayPlan(
    @PrimaryKey
    val dayNumber: Int,
    val title: String,
    val phase: Int, // 1: Foundation (1-10), 2: Speed (11-20), 3: Simulation (21-30)
    val quantTopic: String,
    val reasoningTopic: String,
    val englishTopic: String,
    val gaTopic: String,
    val mockTestName: String,
    val isCompleted: Boolean = false,
    val quantCompleted: Boolean = false,
    val reasoningCompleted: Boolean = false,
    val lunchCompleted: Boolean = false,
    val mockCompleted: Boolean = false,
    val mockAnalysisCompleted: Boolean = false,
    val englishCompleted: Boolean = false,
    val walkingCompleted: Boolean = false,
    val gaCompleted: Boolean = false,
    val dinnerCompleted: Boolean = false,
    val revisionCompleted: Boolean = false,
    val userNotes: String = ""
) {
    fun completedTaskCount(): Int {
        var count = 0
        if (quantCompleted) count++
        if (reasoningCompleted) count++
        if (lunchCompleted) count++
        if (mockCompleted) count++
        if (mockAnalysisCompleted) count++
        if (englishCompleted) count++
        if (walkingCompleted) count++
        if (gaCompleted) count++
        if (dinnerCompleted) count++
        if (revisionCompleted) count++
        return count
    }

    fun totalTaskCount(): Int = 10

    fun progressPercent(): Float = completedTaskCount().toFloat() / 10f

    val completedBlocksCount: Int
        get() = completedTaskCount()
}
