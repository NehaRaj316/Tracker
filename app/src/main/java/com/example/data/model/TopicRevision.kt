package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RevisionStatus(val displayName: String) {
    NOT_STARTED("Not Started"),
    NEEDS_REVIEW("Needs Review"),
    MASTERED("Mastered");

    companion object {
        fun fromString(value: String): RevisionStatus {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true)
            } ?: NOT_STARTED
        }
    }
}

@Entity(tableName = "topic_revisions")
data class TopicRevision(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayNumber: Int, // 1..31
    val subject: String, // "Quantitative Aptitude", "Reasoning", "English", "General Awareness"
    val topicTitle: String,
    val status: String = "Not Started", // 'Mastered', 'Needs Review', 'Not Started'
    val notes: String = "",
    val lastRevisedTimestamp: Long = System.currentTimeMillis()
) {
    val revisionStatus: RevisionStatus
        get() = RevisionStatus.fromString(status)
}
