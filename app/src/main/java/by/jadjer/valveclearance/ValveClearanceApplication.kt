package by.jadjer.valveclearance

import android.app.Application
import by.jadjer.valveclearance.data.repository.ValveClearanceRepositoryImpl

class ValveClearanceApplication : Application() {
    val valveClearanceRepository: ValveClearanceRepositoryImpl by lazy { ValveClearanceRepositoryImpl() }
}