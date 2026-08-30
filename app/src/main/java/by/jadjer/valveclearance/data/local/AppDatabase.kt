package by.jadjer.valveclearance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import by.jadjer.valveclearance.data.local.dao.VehicleDao
import by.jadjer.valveclearance.data.local.entity.VehicleEntity
import by.jadjer.valveclearance.data.local.entity.MeasurementSessionEntity
import by.jadjer.valveclearance.data.local.entity.ValveMeasurementEntity

@Database(
    entities = [
        VehicleEntity::class,
        MeasurementSessionEntity::class,
        ValveMeasurementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}
