package by.jadjer.valveclearance.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import by.jadjer.valveclearance.App
import by.jadjer.valveclearance.ui.screen.EngineParamsScreen
import by.jadjer.valveclearance.ui.screen.MeasurementsScreen
import by.jadjer.valveclearance.ui.screen.ResultsScreen
import by.jadjer.valveclearance.ui.screen.ServiceLimitsScreen
import by.jadjer.valveclearance.ui.screen.WelcomeScreen

@Composable
fun AppNavGraph(app: App) {
    val navController = rememberNavController()
    val repository = app.valveClearanceRepository

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onNext = { navController.navigate("engineParams") }
            )
        }
        composable("engineParams") {
            EngineParamsScreen(
                repository = repository,
                onNext = { navController.navigate("serviceLimits") }
            )
        }
        composable("serviceLimits") {
            ServiceLimitsScreen(
                repository = repository,
                onNext = { navController.navigate("measuredClearances") }
            )
        }
        composable("measuredClearances") {
            MeasurementsScreen(
                repository = repository,
                onNext = { navController.navigate("results") }
            )
        }
        composable("results") {
            ResultsScreen(
                repository = repository,
            )
        }
    }
}
