package com.example.examenmultimedia.View

import com.example.examenmultimedia.ViewModel.VM.Media.MediaViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.examenmultimedia.Model.Utils.MediaPlayerController
import com.example.examenmultimedia.View.AppNavigation.AppNavigation
import com.example.examenmultimedia.Model.Utils.MediaRepository
import com.example.examenmultimedia.Model.Permission.RequestPermissionEffect
import com.example.examenmultimedia.ViewModel.VM.VideoCapture.CaptureViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaRepository = MediaRepository(this)
        val mediaViewModel = MediaViewModel(mediaRepository)
        val mediaPlayerController = MediaPlayerController(this)
        val captureViewModel = CaptureViewModel()
        setContent {
            val navController = rememberNavController()
            RequestPermissionEffect()
            AppNavigation(navController, mediaViewModel, mediaPlayerController,captureViewModel)
        }
    }
}
