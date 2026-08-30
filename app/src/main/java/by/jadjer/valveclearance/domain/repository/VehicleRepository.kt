package by.jadjer.valveclearance.domain.repository

import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.valveclearance.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun getAllVehicles(): Flow<List<Vehicle>>
    suspend fun getVehicleById(id: Long): Vehicle?
    suspend fun addVehicle(vehicle: Vehicle)
    suspend fun updateVehicle(vehicle: Vehicle)
    suspend fun deleteVehicle(vehicle: Vehicle)
    
    suspend fun saveMeasurementSession(vehicle: Vehicle, measurements: List<ValveMeasurement>)
    fun getSessionsForVehicle(vehicleId: Long): Flow<List<MeasurementSession>>
    suspend fun getMeasurementsForSession(sessionId: Long): List<ValveMeasurement>
    suspend fun deleteSession(sessionId: Long)
}

data class MeasurementSession(
    val id: Long,
    val vehicleId: Long,
    val timestamp: Long,
    val notes: String
)