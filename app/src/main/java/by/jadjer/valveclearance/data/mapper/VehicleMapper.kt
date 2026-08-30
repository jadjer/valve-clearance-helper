package by.jadjer.valveclearance.data.mapper

import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveType
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.valveclearance.data.local.entity.VehicleEntity
import by.jadjer.valveclearance.data.local.entity.ValveMeasurementEntity
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.model.Engine
import by.jadjer.valveclearance.domain.model.Cylinder

fun VehicleEntity.toDomainModel(): Vehicle {
    return Vehicle(
        id = id,
        brand = brand,
        model = model,
        year = year,
        engine = Engine(List(cylindersCount) { Cylinder(intakePerCylinder, exhaustPerCylinder) }),
        specification = ValveSpecification(intakeMin, intakeMax, exhaustMin, exhaustMax)
    )
}

fun Vehicle.toEntity(): VehicleEntity {
    val cylinder = engine.cylinders.firstOrNull()
    return VehicleEntity(
        id = id,
        brand = brand,
        model = model,
        year = year,
        cylindersCount = engine.cylinders.size,
        intakePerCylinder = cylinder?.intakePerCylinder ?: 0,
        exhaustPerCylinder = cylinder?.exhaustPerCylinder ?: 0,
        intakeMin = specification.intakeMin,
        intakeMax = specification.intakeMax,
        exhaustMin = specification.exhaustMin,
        exhaustMax = specification.exhaustMax
    )
}

fun ValveMeasurementEntity.toDomainModel(): ValveMeasurement {
    return ValveMeasurement(
        valveNumber = valveNumber,
        valveType = ValveType.valueOf(valveType),
        clearance = clearance,
        shim = Shim(valveNumber, shimSize)
    )
}

fun ValveMeasurement.toEntity(sessionId: Long): ValveMeasurementEntity {
    return ValveMeasurementEntity(
        sessionId = sessionId,
        valveNumber = valveNumber,
        valveType = valveType.name,
        clearance = clearance,
        shimSize = shim.size
    )
}
