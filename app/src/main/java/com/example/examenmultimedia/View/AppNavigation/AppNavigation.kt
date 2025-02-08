package com.example.examenmultimedia.View.AppNavigation

import MediaViewModel
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.examenmultimedia.Model.Utils.MediaPlayerController
import com.example.examenmultimedia.View.CaptureScreen
import com.example.examenmultimedia.View.DetailScreen.DetailScreen
import com.example.examenmultimedia.View.HomScreen.HomeScreen
import com.example.examenmultimedia.View.MainScreen.MainScreen
import com.example.examenmultimedia.ViewModel.Media.CaptureViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MediaViewModel,
    mediaPlayerController: MediaPlayerController,
    captureViewModel: CaptureViewModel // ✅ Agregado
) {
    NavHost(navController = navController, startDestination = "homeScreen") {

        composable("homeScreen") {
            HomeScreen(navController)
        }

        composable("mainScreen") {
            MainScreen(navController, viewModel)
        }

        composable("captureScreen") {
            CaptureScreen(navController, captureViewModel) // ✅ Corrección: Pasar `captureViewModel`
        }

        composable("detailScreen/{uri}/{title}") { backStackEntry ->
            val uri = Uri.decode(backStackEntry.arguments?.getString("uri") ?: "")
            val title = backStackEntry.arguments?.getString("title") ?: "Sin título"

            DetailScreen(uri, title, navController, mediaPlayerController)
        }
    }
}
