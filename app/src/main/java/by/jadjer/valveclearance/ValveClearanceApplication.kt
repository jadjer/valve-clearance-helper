package by.jadjer.valveclearance

import android.app.Application
import by.jadjer.valveclearance.di.AppContainer
import by.jadjer.valveclearance.di.AppDataContainer

class ValveClearanceApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
