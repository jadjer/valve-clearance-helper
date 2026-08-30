package by.jadjer.valveclearance.di

import android.content.Context
import androidx.room.Room
import by.jadjer.valveclearance.data.local.AppDatabase
import by.jadjer.valveclearance.data.repository.ValveClearanceRepositoryImpl
import by.jadjer.valveclearance.data.repository.VehicleRepositoryImpl
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.domain.repository.VehicleRepository

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val vehicleRepository: VehicleRepository
    val valveClearanceRepository: ValveClearanceRepository
}

/**
 * [AppContainer] implementation that provides instance of [VehicleRepositoryImpl] 
 * and [ValveClearanceRepositoryImpl]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AppDatabase]
     */
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "valve_clearance.db")
            .build()
    }

    /**
     * Implementation for [VehicleRepository]
     */
    override val vehicleRepository: VehicleRepository by lazy {
        VehicleRepositoryImpl(database.vehicleDao())
    }

    /**
     * Implementation for [ValveClearanceRepository]
     */
    override val valveClearanceRepository: ValveClearanceRepository by lazy {
        ValveClearanceRepositoryImpl()
    }
}
