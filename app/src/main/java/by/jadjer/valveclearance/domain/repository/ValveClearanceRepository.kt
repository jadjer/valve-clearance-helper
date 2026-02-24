package by.jadjer.valveclearance.domain.repository

import android.hardware.usb.UsbDevice
import by.jadjer.shimcalculator.ClearanceCalculator
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType
import kotlinx.coroutines.flow.StateFlow

interface ValveClearanceRepository {
    val cylinders: StateFlow<Int>
    val intakeValves: StateFlow<Int>
    val exhaustValves: StateFlow<Int>
    val measurements: StateFlow<List<ValveMeasurement>>
    val specification: StateFlow<ValveSpecification>

    fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int);
    fun setClearanceLimit(intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float);
    fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float);
    fun getAdjustedValves(): List<Instruction>;
}
