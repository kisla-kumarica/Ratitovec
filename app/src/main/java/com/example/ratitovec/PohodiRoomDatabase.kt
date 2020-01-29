package com.example.ratitovec

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pohod::class], version = 1, exportSchema = false)
abstract class PohodiRoomDatabase : RoomDatabase() {
    abstract fun PohodDAO(): PohodDAO

    companion object {
        private var INSTANCE: PohodiRoomDatabase? = null
        fun getDatabase(context: Context): PohodiRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(PohodiRoomDatabase::class.java) {
                    if (INSTANCE == null) {

                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PohodiRoomDatabase::class.java, "pohod_database.db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }

    }
}