package by.jadjer.valveclearance.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import by.jadjer.valveclearance.ValveClearanceApplication
import by.jadjer.valveclearance.ui.screen.*

@Composable
fun AppNavGraph(app: ValveClearanceApplication) {
    val navController = rememberNavController()
    val sessionRepo = app.container.valveClearanceRepository
    val vehicleRepo = app.container.vehicleRepository

    NavHost(
        navController = navController,
        startDestination = "vehicle_list"
    ) {
        // --- Vehicle Management ---
        
        composable("vehicle_list") {
            VehicleListScreen(
                repository = vehicleRepo,
                onAddVehicle = { navController.navigate("vehicle_form") },
                onVehicleClick = { id -> navController.navigate("vehicle_history/$id") },
                onQuickCalculate = { navController.navigate("welcome") }
            )
        }

        composable(
            route = "vehicle_form?vehicleId={vehicleId}",
            arguments = listOf(navArgument("vehicleId") { 
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: -1L
            VehicleFormScreen(
                repository = vehicleRepo,
                vehicleId = vehicleId,
                onVehicleSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "vehicle_history/{vehicleId}",
            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: 0L
            VehicleHistoryScreen(
                vehicleId = vehicleId,
                repository = vehicleRepo,
                sessionRepository = sessionRepo,
                onSessionClick = { id -> navController.navigate("session_details/$id") },
                onNewMeasurement = { navController.navigate("measured_clearances?vehicleId=$vehicleId") },
                onEditVehicle = { id -> navController.navigate("vehicle_form?vehicleId=$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "session_details/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            SessionDetailsScreen(
                sessionId = sessionId,
                repository = vehicleRepo,
                onDelete = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // --- Calculation Flow ---

        composable("welcome") {
            WelcomeScreen(
                onNext = { 
                    sessionRepo.reset()
                    navController.navigate("engine_params") 
                }
            )
        }

        composable(
            route = "engine_params?vehicleId={vehicleId}",
            arguments = listOf(navArgument("vehicleId") { 
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: -1L
            EngineParamsScreen(
                repository = sessionRepo,
                vehicleRepository = vehicleRepo,
                vehicleId = vehicleId,
                onNext = { navController.navigate("service_limits") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("service_limits") {
            ServiceLimitsScreen(
                repository = sessionRepo,
                onNext = { navController.navigate("measured_clearances") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "measured_clearances?vehicleId={vehicleId}",
            arguments = listOf(navArgument("vehicleId") { 
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: -1L
            MeasurementsScreen(
                repository = sessionRepo,
                onNext = { navController.navigate("results?vehicleId=$vehicleId") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "results?vehicleId={vehicleId}",
            arguments = listOf(navArgument("vehicleId") { 
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: -1L
            ResultsScreen(
                repository = sessionRepo,
                vehicleRepository = vehicleRepo,
                vehicleId = vehicleId,
                onFinish = { 
                    if (vehicleId != -1L) {
                        navController.popBackStack("vehicle_history/$vehicleId", inclusive = false)
                    } else {
                        navController.popBackStack("vehicle_list", inclusive = false)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
