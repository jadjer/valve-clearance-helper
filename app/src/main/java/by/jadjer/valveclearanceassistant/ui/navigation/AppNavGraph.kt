package by.jadjer.valveclearanceassistant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.*
import by.jadjer.valveclearanceassistant.ui.screen.EngineParamsScreen
import by.jadjer.valveclearanceassistant.ui.screen.MeasuredClearancesScreen
import by.jadjer.valveclearanceassistant.ui.screen.ResultsScreen
import by.jadjer.valveclearanceassistant.ui.screen.ServiceLimitsScreen
import by.jadjer.valveclearanceassistant.ui.screen.ShimsInputScreen
import by.jadjer.valveclearanceassistant.ui.screen.WelcomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

//    val viewModel: SumViewModel = ViewModelProvider(viewModelStoreOwner)[SumViewModel::class.java]

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // 1. Экран приветствия
        composable("welcome") {
            WelcomeScreen {
                navController.navigate("engineParams")
            }
        }

        // 2. Экран параметров двигателя
        composable("engineParams") {
            EngineParamsScreen { cylinders, intakeValves, exhaustValves ->
                // Сохраняем параметры в SavedStateHandle
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("cylinders", cylinders)
                    set("intakeValves", intakeValves)
                    set("exhaustValves", exhaustValves)
                }
                navController.navigate("serviceLimits")
            }
        }

        // 3. Экран сервисных лимитов
        composable("serviceLimits") {
            // Получаем параметры из предыдущего экрана
            val cylinders = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("cylinders") ?: 4
            val intakeValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("intakeValves") ?: 2
            val exhaustValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("exhaustValves") ?: 2

            ServiceLimitsScreen { intakeMin, intakeMax, exhaustMin, exhaustMax ->
                // Сохраняем лимиты и передаем дальше
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("intakeMin", intakeMin)
                    set("intakeMax", intakeMax)
                    set("exhaustMin", exhaustMin)
                    set("exhaustMax", exhaustMax)
                }
                navController.navigate("measuredClearances")
            }
        }

        // 4. Экран ввода измеренных зазоров
        composable("measuredClearances") {
            // Получаем все предыдущие параметры
            val cylinders = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("cylinders") ?: 4
            val intakeValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("intakeValves") ?: 2
            val exhaustValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("exhaustValves") ?: 2

            MeasuredClearancesScreen(
                cylinders = cylinders,
                intakeValves = intakeValves,
                exhaustValves = exhaustValves,
                onNext = { intakeClearances, exhaustClearances ->
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("intakeClearances", intakeClearances)
                        set("exhaustClearances", exhaustClearances)
                    }
                    navController.navigate("shimsInput")
                }
            )
        }

        // 5. Экран ввода текущих шайб
        composable("shimsInput") {
            val cylinders = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("cylinders") ?: 4
            val intakeValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("intakeValves") ?: 2
            val exhaustValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("exhaustValves") ?: 2

            ShimsInputScreen(
                cylinders = cylinders,
                intakeValves = intakeValves,
                exhaustValves = exhaustValves,
                onNext = { intakeShims, exhaustShims ->
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("intakeShims", intakeShims)
                        set("exhaustShims", exhaustShims)
                    }
                    navController.navigate("results")
                }
            )
        }

        // 6. Экран результатов
        composable("results") {
            // Собираем все необходимые данные из предыдущих экранов
            val cylinders = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("cylinders") ?: 4
            val intakeValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("intakeValves") ?: 2
            val exhaustValves = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("exhaustValves") ?: 2

            val intakeMin = navController.previousBackStackEntry?.savedStateHandle?.get<Float>("intakeMin") ?: 0.15f
            val intakeMax = navController.previousBackStackEntry?.savedStateHandle?.get<Float>("intakeMax") ?: 0.25f
            val exhaustMin = navController.previousBackStackEntry?.savedStateHandle?.get<Float>("exhaustMin") ?: 0.25f
            val exhaustMax = navController.previousBackStackEntry?.savedStateHandle?.get<Float>("exhaustMax") ?: 0.35f

            val intakeClearances = navController.previousBackStackEntry?.savedStateHandle?.get<List<Float>>("intakeClearances") ?: emptyList()
            val exhaustClearances = navController.previousBackStackEntry?.savedStateHandle?.get<List<Float>>("exhaustClearances") ?: emptyList()

            val intakeShims = navController.previousBackStackEntry?.savedStateHandle?.get<List<Float>>("intakeShims") ?: emptyList()
            val exhaustShims = navController.previousBackStackEntry?.savedStateHandle?.get<List<Float>>("exhaustShims") ?: emptyList()

            ResultsScreen(
                intakeShims = intakeShims,
                exhaustShims = exhaustShims,
                intakeClearances = intakeClearances,
                exhaustClearances = exhaustClearances,
                intakeMin = intakeMin,
                intakeMax = intakeMax,
                exhaustMin = exhaustMin,
                exhaustMax = exhaustMax,
                onRestart = {
                    // Очищаем историю и возвращаемся к первому экрану
                    navController.popBackStack("welcome", inclusive = false)
                }
            )
        }
    }
}
