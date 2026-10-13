package com.example.data.model

enum class PomodoroMode(
    val title: String,
    val durationMinutes: Int,
    val isBreak: Boolean
) {
    POMODORO_25("Pomodoro (25m)", 25, false),
    FULL_SESSION("Full Block", 0, false),
    SHORT_BREAK("Short Break (5m)", 5, true),
    LONG_BREAK("Long Break (15m)", 15, true),
    DEEP_FOCUS_50("Deep Focus (50m)", 50, false)
}
