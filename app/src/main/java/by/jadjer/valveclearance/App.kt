package by.jadjer.valveclearance

import android.app.Application
import by.jadjer.valveclearance.repository.ValveClearanceRepository

class App : Application() {
    val valveClearanceRepository: ValveClearanceRepository by lazy { ValveClearanceRepository() }
}