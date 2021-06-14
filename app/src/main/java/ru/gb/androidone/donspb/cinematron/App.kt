package ru.gb.androidone.donspb.cinematron

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import androidx.room.Room
import ru.gb.androidone.donspb.cinematron.room.RecentDB
import ru.gb.androidone.donspb.cinematron.room.RecentDao

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: RecentDB? = null
        private const val DB_NAME = "RecentMovies.db"

        fun getRecentDao(): RecentDao {
            if (db == null) {
                synchronized(RecentDB::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw
                                IllegalStateException("Application is null")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            RecentDB::class.java,
                            DB_NAME)
                            .build()
                    }
                }
            }
            return db!!.recentDao()
        }
    }
}