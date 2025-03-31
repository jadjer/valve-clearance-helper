package by.jadjer.valveclearanceassistant.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import by.jadjer.valveclearanceassistant.App
import by.jadjer.valveclearanceassistant.ui.screen.EngineParamsScreen
import by.jadjer.valveclearanceassistant.ui.screen.MeasuredClearancesScreen
import by.jadjer.valveclearanceassistant.ui.screen.ResultsScreen
import by.jadjer.valveclearanceassistant.ui.screen.ServiceLimitsScreen
import by.jadjer.valveclearanceassistant.ui.screen.ShimsInputScreen
import by.jadjer.valveclearanceassistant.ui.screen.WelcomeScreen

@Composable
fun AppNavGraph(app: App) {
    val navController = rememberNavController()
    val repository = app.valveClearanceRepository

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // 1. Экран приветствия
        composable("welcome") {
            WelcomeScreen(
                onNext = { navController.navigate("engineParams") }
            )
        }

        // 2. Экран параметров двигателя
        composable("engineParams") {
            EngineParamsScreen(
                repository = repository,
                onNext = { navController.navigate("serviceLimits") }
            )
        }

        // 3. Экран сервисных лимитов
        composable("serviceLimits") {
            ServiceLimitsScreen(
                repository = repository,
                onNext = { navController.navigate("measuredClearances") }
            )
        }

        // 4. Экран ввода измеренных зазоров
        composable("measuredClearances") {
            MeasuredClearancesScreen(
                repository = repository,
                onNext = { navController.navigate("shimsInput") }
            )
        }

        // 5. Экран ввода текущих шайб
        composable("shimsInput") {
            ShimsInputScreen(
                repository = repository,
                onNext = { navController.navigate("results") }
            )
        }

        // 6. Экран результатов
        composable("results") {
            ResultsScreen(
                repository = repository,
                onRestart = { navController.popBackStack("welcome", inclusive = false) }
            )
        }
    }
}
