package by.jadjer.valveclearanceassistant

import android.app.Application
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository

class App : Application() {
    val valveClearanceRepository: ValveClearanceRepository by lazy { ValveClearanceRepository() }
}