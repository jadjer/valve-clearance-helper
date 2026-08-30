package by.jadjer.valveclearance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val cylindersCount: Int,
    val intakePerCylinder: Int,
    val exhaustPerCylinder: Int,
    val intakeMin: Float,
    val intakeMax: Float,
    val exhaustMin: Float,
    val exhaustMax: Float,
    val lastCheckTimestamp: Long = 0
)
