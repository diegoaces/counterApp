package cl.diegoacuna.counterapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Counter::class], version = 1)
abstract class CountersDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao

    companion object {
        private const val DATABASE_NAME = "counter_database"
        @Volatile
        private var INSTANCE: CountersDatabase? = null

        fun getInstance(context: Context): CountersDatabase? {

            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CountersDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }
}