package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.utils.formatTimestamp

@Composable
fun VehicleItem(
    vehicle: Vehicle,
    lastCheck: Long
) {
    val formattedDate = remember(lastCheck) { formatTimestamp(lastCheck) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = vehicle.brand, style = MaterialTheme.typography.titleLarge)
                Text(text = vehicle.year.toString(), style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = vehicle.model, style = MaterialTheme.typography.bodyMedium)
                Text(text = formattedDate, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleItemPreview() {
    val now = System.currentTimeMillis()

    VehicleItem(
        vehicle = Vehicle(
            brand = "Honda", model = "XL1000V Varadero", year = 2008,
        ),
        lastCheck = now,
    )
}
