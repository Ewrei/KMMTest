package robin.morty.kmm

import android.app.Application
import com.surrus.common.di.initKoin
import robin.morty.kmm.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MortyApplication)
            modules(appModule)
        }
    }

}