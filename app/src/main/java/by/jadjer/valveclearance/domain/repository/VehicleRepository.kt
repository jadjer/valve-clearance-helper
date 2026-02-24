package by.jadjer.valveclearance.domain.repository

import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.valveclearance.domain.model.Vehicle

interface VehicleRepository {
    fun getVehicles(): List<Vehicle>;
    fun createVehicle(vehicle: Vehicle);

    fun updateVehicleClearance(vehicle: Vehicle, intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float);
    fun updateVehicleMeasuredValue(vehicle: Vehicle, valveNumber: Int, clearance: Float, shim: Float)

    fun calculateAdjustedValves(): List<Instruction>;
}