package br.com.printk.ufmtnews

import android.app.Application
import br.com.printk.ufmtnews.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class UfmtNewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UfmtNewsApp)
            androidLogger()
            modules(appModule)
        }
    }
}
