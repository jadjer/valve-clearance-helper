package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository

class MeasuredClearancesViewModel(private val repository: ValveClearanceRepository) : ViewModel() {
    fun getCylinders() : Int {
        return repository.getEngineData().cylinders
    }

    fun getIntakeValves() : Int {
        return repository.getEngineData().intakeValves
    }

    fun getExhaustValves() : Int {
        return repository.getEngineData().exhaustValves
    }
}

class MeasuredClearancesViewModelFactory(private val repository: ValveClearanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeasuredClearancesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeasuredClearancesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
