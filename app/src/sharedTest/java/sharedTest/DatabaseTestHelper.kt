package sharedTest

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseTestHelper {

    fun <T: RoomDatabase> initializeDb(context: Context, clazz: Class<T>): T {
        return Room.inMemoryDatabaseBuilder(context, clazz)
            .allowMainThreadQueries()
            .build()
    }
}