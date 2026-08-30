package by.jadjer.valveclearance.data.local.dao

import androidx.room.*
import by.jadjer.valveclearance.data.local.entity.VehicleEntity
import by.jadjer.valveclearance.data.local.entity.MeasurementSessionEntity
import by.jadjer.valveclearance.data.local.entity.ValveMeasurementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles ORDER BY lastCheckTimestamp DESC")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: Long): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE brand = :brand AND model = :model AND year = :year")
    suspend fun getVehicleByDetails(brand: String, model: String, year: Int): List<VehicleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity): Long

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleEntity)

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Insert
    suspend fun insertSession(session: MeasurementSessionEntity): Long

    @Insert
    suspend fun insertMeasurements(measurements: List<ValveMeasurementEntity>)

    @Query("SELECT * FROM measurement_sessions WHERE vehicleId = :vehicleId ORDER BY timestamp DESC")
    fun getSessionsForVehicle(vehicleId: Long): Flow<List<MeasurementSessionEntity>>

    @Query("SELECT * FROM valve_measurements WHERE sessionId = :sessionId")
    suspend fun getMeasurementsForSession(sessionId: Long): List<ValveMeasurementEntity>

    @Query("DELETE FROM measurement_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: Long)
}
