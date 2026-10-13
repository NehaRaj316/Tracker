package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DayPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface DayPlanDao {
    @Query("SELECT * FROM day_plans ORDER BY dayNumber ASC")
    fun getAllDayPlans(): Flow<List<DayPlan>>

    @Query("SELECT * FROM day_plans WHERE dayNumber = :dayNumber")
    fun getDayPlanByNumber(dayNumber: Int): Flow<DayPlan?>

    @Query("SELECT * FROM day_plans WHERE dayNumber = :dayNumber")
    suspend fun getDayPlanSync(dayNumber: Int): DayPlan?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dayPlans: List<DayPlan>)

    @Update
    suspend fun updateDayPlan(dayPlan: DayPlan)

    @Query("SELECT COUNT(*) FROM day_plans")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM day_plans WHERE isCompleted = 1")
    fun getCompletedDaysCount(): Flow<Int>

    @Query("UPDATE day_plans SET userNotes = :notes WHERE dayNumber = :dayNumber")
    suspend fun updateNotes(dayNumber: Int, notes: String)

    @Query("UPDATE day_plans SET quantCompleted = :completed WHERE dayNumber = :dayNumber")
    suspend fun updateQuantCompleted(dayNumber: Int, completed: Boolean)

    @Query("UPDATE day_plans SET reasoningCompleted = :completed WHERE dayNumber = :dayNumber")
    suspend fun updateReasoningCompleted(dayNumber: Int, completed: Boolean)

    @Query("UPDATE day_plans SET englishCompleted = :completed WHERE dayNumber = :dayNumber")
    suspend fun updateEnglishCompleted(dayNumber: Int, completed: Boolean)

    @Query("UPDATE day_plans SET gaCompleted = :completed WHERE dayNumber = :dayNumber")
    suspend fun updateGaCompleted(dayNumber: Int, completed: Boolean)

    @Query("UPDATE day_plans SET mockCompleted = :completed WHERE dayNumber = :dayNumber")
    suspend fun updateMockCompleted(dayNumber: Int, completed: Boolean)

    @Query("UPDATE day_plans SET revisionCompleted = :completed WHERE dayNumber = :dayNumber")
    suspend fun updateRevisionCompleted(dayNumber: Int, completed: Boolean)
}
