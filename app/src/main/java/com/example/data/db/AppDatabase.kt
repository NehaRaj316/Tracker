package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.DayPlan
import com.example.data.model.MockTest
import com.example.data.model.TopicRevision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [DayPlan::class, MockTest::class, TopicRevision::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dayPlanDao(): DayPlanDao
    abstract fun mockTestDao(): MockTestDao
    abstract fun topicRevisionDao(): TopicRevisionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ssc_cgl_planner_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate with 31-day curriculum, mock series, and subject revision roadmap
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.dayPlanDao().insertAll(InitialData.get31DayPlans())
                                    database.mockTestDao().insertAll(InitialData.get31MockTests())
                                    database.topicRevisionDao().insertAll(InitialData.getDefaultTopicRevisions())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
