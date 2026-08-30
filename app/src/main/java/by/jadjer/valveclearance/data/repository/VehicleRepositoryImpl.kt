package by.jadjer.valveclearance.data.repository

import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.valveclearance.data.local.dao.VehicleDao
import by.jadjer.valveclearance.data.local.entity.MeasurementSessionEntity
import by.jadjer.valveclearance.data.mapper.toDomainModel
import by.jadjer.valveclearance.data.mapper.toEntity
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.repository.MeasurementSession
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VehicleRepositoryImpl(private val vehicleDao: VehicleDao) : VehicleRepository {

    override fun getAllVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.getAllVehicles().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getVehicleById(id: Long): Vehicle? {
        return vehicleDao.getVehicleById(id)?.toDomainModel()
    }

    override suspend fun addVehicle(vehicle: Vehicle) {
        vehicleDao.insertVehicle(vehicle.toEntity())
    }

    override suspend fun updateVehicle(vehicle: Vehicle) {
        vehicleDao.updateVehicle(vehicle.toEntity())
    }

    override suspend fun deleteVehicle(vehicle: Vehicle) {
        vehicleDao.deleteVehicle(vehicle.toEntity())
    }

    override suspend fun saveMeasurementSession(vehicle: Vehicle, measurements: List<ValveMeasurement>) {
        val sessionId = vehicleDao.insertSession(
            MeasurementSessionEntity(
                vehicleId = vehicle.id,
                timestamp = System.currentTimeMillis()
            )
        )
        
        val measurementEntities = measurements.map { 
            it.toEntity(sessionId)
        }
        vehicleDao.insertMeasurements(measurementEntities)
        
        // Update last check timestamp
        val entity = vehicle.toEntity().copy(lastCheckTimestamp = System.currentTimeMillis())
        vehicleDao.updateVehicle(entity)
    }

    override fun getSessionsForVehicle(vehicleId: Long): Flow<List<MeasurementSession>> {
        return vehicleDao.getSessionsForVehicle(vehicleId).map { entities ->
            entities.map { entity ->
                MeasurementSession(
                    id = entity.id,
                    vehicleId = entity.vehicleId,
                    timestamp = entity.timestamp,
                    notes = entity.notes
                )
            }
        }
    }

    override suspend fun getMeasurementsForSession(sessionId: Long): List<ValveMeasurement> {
        return vehicleDao.getMeasurementsForSession(sessionId).map { it.toDomainModel() }
    }

    override suspend fun deleteSession(sessionId: Long) {
        vehicleDao.deleteSession(sessionId)
    }
}
