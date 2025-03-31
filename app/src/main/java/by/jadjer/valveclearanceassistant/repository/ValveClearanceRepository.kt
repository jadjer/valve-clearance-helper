package by.jadjer.valveclearanceassistant.repository

import by.jadjer.valveclearanceassistant.model.domain.EngineData

class ValveClearanceRepository {

    private val engineData: EngineData = EngineData(1, 1, 1);

    fun getEngineData(): EngineData {
        return engineData
    }

    fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {
        engineData.cylinders = cylinders
        engineData.intakeValves = intakeValves
        engineData.exhaustValves = exhaustValves
    }

    fun setIntakeClearance(intakeClearanceMin: Float, intakeClearanceMax: Float) {
        print(intakeClearanceMin)
        print(intakeClearanceMax)
    }

    fun setExhaustClearance(exhaustClearanceMin: Float, exhaustClearanceMax: Float) {
        print(exhaustClearanceMin)
        print(exhaustClearanceMax)
    }
}