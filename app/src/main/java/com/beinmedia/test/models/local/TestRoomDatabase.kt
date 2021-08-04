package com.beinmedia.test.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.beinmedia.test.models.local.dao.NewsDBDao
import com.beinmedia.test.models.local.models.NewsDB
import java.util.concurrent.Executors

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(NewsDB::class), version = 1, exportSchema = false)
public abstract class TestRoomDatabase : RoomDatabase() {

    abstract fun newsDBDao(): NewsDBDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TestRoomDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context): TestRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestRoomDatabase::class.java,
                    "test_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}