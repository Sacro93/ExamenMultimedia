package com.example.examenmultimedia.View.AppNavigation

import MediaViewModel
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examenmultimedia.View.DetailScreen.DetailScreen
import com.example.examenmultimedia.View.MainScreen.MainScreen


/*---------------¿Qué hace este código?

NavHost maneja la navegación entre mainScreen (lista) y detailScreen (reproductor).
Pasamos el URI y el título del archivo multimedia a la pantalla de detalles.
*/
@Composable
fun AppNavigation(viewModel: MediaViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") {
            MainScreen(navController, viewModel)
        }
        composable("detailScreen/{uri}/{title}") { backStackEntry ->
            val uri = Uri.decode(backStackEntry.arguments?.getString("uri") ?: "")
            val title = backStackEntry.arguments?.getString("title") ?: "Sin título"
            DetailScreen(uri, title, navController, viewModel)
        }
    }
}
