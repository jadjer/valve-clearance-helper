package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EngineParamsViewModel(private val repository: ValveClearanceRepository) : ViewModel() {
    private val _cylinders = MutableStateFlow(1)
    private val _intakeValves = MutableStateFlow(1)
    private val _exhaustValves = MutableStateFlow(1)

    val cylinders: StateFlow<Int> = _cylinders
    val intakeValves: StateFlow<Int> = _intakeValves
    val exhaustValves: StateFlow<Int> = _exhaustValves

    fun setCylinders(value: Int) {
        _cylinders.value = value

        repository.setEngineData(_cylinders.value, _intakeValves.value, _exhaustValves.value)
    }

    fun setIntakeValves(value: Int) {
        _intakeValves.value = value

        repository.setEngineData(_cylinders.value, _intakeValves.value, _exhaustValves.value)
    }

    fun setExhaustValves(value: Int) {
        _exhaustValves.value = value

        repository.setEngineData(_cylinders.value, _intakeValves.value, _exhaustValves.value)
    }
}

class EngineParamsViewModelFactory(private val repository: ValveClearanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EngineParamsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EngineParamsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
