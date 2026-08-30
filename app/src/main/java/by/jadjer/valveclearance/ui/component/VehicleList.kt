package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.jadjer.valveclearance.domain.model.Vehicle

@Composable
fun VehicleList(
    vehicles: List<Vehicle>,
    modifier: Modifier = Modifier,
    onVehicleClick: (Vehicle) -> Unit = {},
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(vehicles) { vehicle ->
            VehicleItem(
                vehicle = vehicle, 
                lastCheck = 0, // Should be part of Vehicle model or passed separately
                onClick = { onVehicleClick(vehicle) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleListPreview() {
    val mockSpec = by.jadjer.shimcalculator.models.ValveSpecification(0.15f, 0.20f, 0.20f, 0.25f)
    val mockEngine = by.jadjer.valveclearance.domain.model.Engine(
        listOf(by.jadjer.valveclearance.domain.model.Cylinder(2, 2))
    )
    
    VehicleList(
        listOf(
            Vehicle(brand = "Honda", model = "XL1000V Varadero", year = 2008, engine = mockEngine, specification = mockSpec),
            Vehicle(brand = "Subaru", model = "Tribeca B9", year = 2006, engine = mockEngine, specification = mockSpec),
        )
    )
}
