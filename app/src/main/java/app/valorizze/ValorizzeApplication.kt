package app.valorizze

import android.app.Application
import app.valorizze.core.context.AppContextProvider
import app.valorizze.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class ValorizzeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
        initializeKoin {
            androidContext(this@ValorizzeApplication)
        }
    }
}

