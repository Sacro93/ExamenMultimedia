package com.example.examenmultimedia.View

import MediaViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import com.example.examenmultimedia.View.AppNavigation.AppNavigation
import com.example.examenmultimedia.Model.MediaRepository
import com.example.examenmultimedia.ViewModel.MediaViewModelFactory


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaRepository = MediaRepository(this)
        val exoPlayer = ExoPlayer.Builder(this).build()

        val viewModelFactory = MediaViewModelFactory(mediaRepository, exoPlayer)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MediaViewModel::class.java) // FIX

        setContent {
            RequestPermissionEffect() // FIX: Asegurar que se llama aquí correctamente
            AppNavigation(viewModel)
        }
    }
}
