package by.jadjer.shimcalculator.models

data class AdjustmentSolution(
    val id: String,
    val requiredShims: List<Shim>,
    val swaps: List<ShimSwap>,
    val totalCost: Float
) {
    val newShimsCount: Int = requiredShims.count { !it.isInstalled }
    val swapsCount: Int = swaps.size
}

data class ShimSwap(
    val fromValve: Int,
    val toValve: Int,
    val shim: Shim
)