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

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // 1. Экран приветствия
        composable("welcome") {
            WelcomeScreen(
                app = app,
                onNext = { navController.navigate("engineParams") }
            )
        }

        // 2. Экран параметров двигателя
        composable("engineParams") {
            EngineParamsScreen(
                app = app,
                onNext = { navController.navigate("serviceLimits") }
            )
        }

        // 3. Экран сервисных лимитов
        composable("serviceLimits") {
            ServiceLimitsScreen(
                app = app,
                onNext = { navController.navigate("measuredClearances") }
            )
        }

        // 4. Экран ввода измеренных зазоров
        composable("measuredClearances") {
            MeasuredClearancesScreen(
                app = app,
                onNext = { navController.navigate("shimsInput") }
            )
        }

        // 5. Экран ввода текущих шайб
        composable("shimsInput") {
            ShimsInputScreen(
                app = app,
                onNext = { navController.navigate("results") }
            )
        }

        // 6. Экран результатов
        composable("results") {
            ResultsScreen(
                app = app,
                onRestart = { navController.popBackStack("welcome", inclusive = false) }
            )
        }
    }
}
