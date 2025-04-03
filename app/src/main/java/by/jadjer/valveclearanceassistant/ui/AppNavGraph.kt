package by.jadjer.valveclearanceassistant.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import by.jadjer.valveclearanceassistant.App
import by.jadjer.valveclearanceassistant.ui.screen.EngineParamsScreen
import by.jadjer.valveclearanceassistant.ui.screen.MeasurementsScreen
import by.jadjer.valveclearanceassistant.ui.screen.ResultsScreen
import by.jadjer.valveclearanceassistant.ui.screen.ServiceLimitsScreen
import by.jadjer.valveclearanceassistant.ui.screen.WelcomeScreen

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
                onRestart = { navController.popBackStack("welcome", inclusive = false) }
            )
        }
    }
}
