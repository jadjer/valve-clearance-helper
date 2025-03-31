package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository

class ShimsInputViewModel(private val repository: ValveClearanceRepository) : ViewModel() {
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

class ShimsInputViewModelFactory(private val repository: ValveClearanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShimsInputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShimsInputViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
