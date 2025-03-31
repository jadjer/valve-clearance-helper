package by.jadjer.shimcalculator.models

data class Shim(
    val size: Float,
    val diameter: Float,
    val material: String = "Steel",
    val isInstalled: Boolean = false
) {
    fun copyWithNewSize(newSize: Float): Shim {
        return this.copy(size = newSize)
    }
}