package com.sifat.mydiary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        @Volatile
        private var database: NoteDatabase? = null

        fun getDB(context: Context): NoteDatabase {
            return database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "Note-DB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database = instance
                instance
            }
        }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Note ADD COLUMN newColumnName TEXT")
            }
        }
    }
