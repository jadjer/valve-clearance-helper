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
fun VehicleList(vehicles: List<Vehicle>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(vehicles) { vehicle ->
            VehicleItem(vehicle = vehicle, lastCheck = 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleListPreview() {
    VehicleList(
        listOf(
            Vehicle(brand = "Honda", model = "XL1000V Varadero", year = 2008),
            Vehicle(brand = "Subaru", model = "Tribeca B9", year = 2006),
        )
    )
}