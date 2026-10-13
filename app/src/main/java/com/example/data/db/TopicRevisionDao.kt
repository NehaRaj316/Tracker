package com.example.data.db

import androidx.room.*
import com.example.data.model.TopicRevision
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicRevisionDao {
    @Query("SELECT * FROM topic_revisions ORDER BY dayNumber ASC, id ASC")
    fun getAllTopicRevisions(): Flow<List<TopicRevision>>

    @Query("SELECT * FROM topic_revisions WHERE dayNumber = :dayNumber ORDER BY id ASC")
    fun getTopicRevisionsForDay(dayNumber: Int): Flow<List<TopicRevision>>

    @Query("SELECT * FROM topic_revisions WHERE subject = :subject ORDER BY dayNumber ASC, id ASC")
    fun getTopicRevisionsForSubject(subject: String): Flow<List<TopicRevision>>

    @Query("SELECT * FROM topic_revisions WHERE status = :status ORDER BY dayNumber ASC, id ASC")
    fun getTopicRevisionsByStatus(status: String): Flow<List<TopicRevision>>

    @Query("SELECT COUNT(*) FROM topic_revisions")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(revisions: List<TopicRevision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(revision: TopicRevision): Long

    @Update
    suspend fun update(revision: TopicRevision)

    @Query("UPDATE topic_revisions SET status = :status, lastRevisedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE topic_revisions SET notes = :notes, lastRevisedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateNotes(id: Long, notes: String, timestamp: Long = System.currentTimeMillis())

    @Delete
    suspend fun delete(revision: TopicRevision)

    @Query("DELETE FROM topic_revisions WHERE id = :id")
    suspend fun deleteById(id: Long)
}
