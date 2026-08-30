package by.jadjer.valveclearance.domain.model

import by.jadjer.shimcalculator.models.ValveSpecification

data class Vehicle(
    val id: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val engine: Engine,
    val specification: ValveSpecification,
)
