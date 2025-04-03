package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EngineParamsViewModel(private val repository: ValveClearanceRepository) : ViewModel() {

    private val _cylinders = MutableStateFlow(repository.cylinders)
    private val _intakeValves = MutableStateFlow(repository.intakeValves)
    private val _exhaustValves = MutableStateFlow(repository.exhaustValves)

    val cylinders: StateFlow<Int> = _cylinders.asStateFlow()
    val intakeValves: StateFlow<Int> = _intakeValves.asStateFlow()
    val exhaustValves: StateFlow<Int> = _exhaustValves.asStateFlow()

    fun setCylinders(value: Int) {
        _cylinders.value = value
    }

    fun setIntakeValves(value: Int) {
        _intakeValves.value = value
    }

    fun setExhaustValves(value: Int) {
        _exhaustValves.value = value
    }

    fun saveData() : Boolean {
        repository.setEngineData(_cylinders.value, _intakeValves.value, _exhaustValves.value)
        return true
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
