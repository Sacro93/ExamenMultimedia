package com.example.examenmultimedia.View

import MediaViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.examenmultimedia.Model.Utils.MediaPlayerController
import com.example.examenmultimedia.View.AppNavigation.AppNavigation
import com.example.examenmultimedia.Model.Utils.MediaRepository
import com.example.examenmultimedia.ViewModel.Media.CaptureViewModel


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
