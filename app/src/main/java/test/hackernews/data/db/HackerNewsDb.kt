package test.hackernews.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import test.hackernews.model.Hit

@Database(
    entities = [Hit::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HackerNewsDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): HackerNewsDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, HackerNewsDb::class.java)
            } else {
                Room.databaseBuilder(context, HackerNewsDb::class.java, "hackerNews.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun posts(): HackerNewsDao
}