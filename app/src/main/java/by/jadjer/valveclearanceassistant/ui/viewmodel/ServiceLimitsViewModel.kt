package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServiceLimitsViewModel(private val repository: ValveClearanceRepository) : ViewModel() {
    private val _intakeClearanceMin = MutableStateFlow<Float>(0.13f)
    private val _intakeClearanceMax = MutableStateFlow<Float>(0.19f)
    private val _exhaustClearanceMin = MutableStateFlow<Float>(0.28f)
    private val _exhaustClearanceMax = MutableStateFlow<Float>(0.34f)

    val intakeClearanceMin: StateFlow<Float> = _intakeClearanceMin.asStateFlow()
    val intakeClearanceMax: StateFlow<Float> = _intakeClearanceMax.asStateFlow()
    val exhaustClearanceMin: StateFlow<Float> = _exhaustClearanceMin.asStateFlow()
    val exhaustClearanceMax: StateFlow<Float> = _exhaustClearanceMax.asStateFlow()

    fun setIntakeClearanceMin(value: Float) {
        _intakeClearanceMin.value = value

        repository.setClearanceLimit(
            _intakeClearanceMin.value,
            _intakeClearanceMax.value,
            _exhaustClearanceMin.value,
            _exhaustClearanceMax.value,
        )
    }

    fun setIntakeClearanceMax(value: Float) {
        _intakeClearanceMax.value = value

        repository.setClearanceLimit(
            _intakeClearanceMin.value,
            _intakeClearanceMax.value,
            _exhaustClearanceMin.value,
            _exhaustClearanceMax.value,
        )
    }

    fun setExhaustClearanceMin(value: Float) {
        _exhaustClearanceMin.value = value

        repository.setClearanceLimit(
            _intakeClearanceMin.value,
            _intakeClearanceMax.value,
            _exhaustClearanceMin.value,
            _exhaustClearanceMax.value,
        )
    }

    fun setExhaustClearanceMax(value: Float) {
        _exhaustClearanceMax.value = value

        repository.setClearanceLimit(
            _intakeClearanceMin.value,
            _intakeClearanceMax.value,
            _exhaustClearanceMin.value,
            _exhaustClearanceMax.value,
        )
    }
}

class ServiceLimitsViewModelFactory(private val repository: ValveClearanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceLimitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceLimitsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
