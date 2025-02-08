package com.example.examenmultimedia.View.AppNavigation

import com.example.examenmultimedia.ViewModel.VM.Media.MediaViewModel
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.examenmultimedia.Model.Utils.MediaPlayerController
import com.example.examenmultimedia.View.Capture.CaptureScreen
import com.example.examenmultimedia.View.DetailScreen.DetailScreen
import com.example.examenmultimedia.View.HomScreen.HomeScreen
import com.example.examenmultimedia.View.VideoScreen.VideoScreen
import com.example.examenmultimedia.ViewModel.VM.VideoCapture.CaptureViewModel


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
            VideoScreen(navController, viewModel)
        }

        composable("captureScreen") {
            CaptureScreen(navController, captureViewModel)
        }

        composable("detailScreen/{uri}/{title}") { backStackEntry ->
            val uri = Uri.decode(backStackEntry.arguments?.getString("uri") ?: "")
            val title = backStackEntry.arguments?.getString("title") ?: "Sin título"

            DetailScreen(uri, title, navController, mediaPlayerController)
        }
    }
}
