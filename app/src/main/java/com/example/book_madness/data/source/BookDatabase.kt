package com.example.book_madness.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Book::class],
    version = 1, exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var Instance: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BookDatabase::class.java, "item_database")
                    /**
                     * deletes all data from the tables in database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
