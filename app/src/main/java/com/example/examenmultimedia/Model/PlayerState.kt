package com.example.examenmultimedia.Model


/*Por qué esto es necesario?

Si la enumeración está mal definida o no es accesible desde com.example.examenmultimedia.ViewModel.VM.Media.MediaViewModel, Android Studio marcará el error en PlayerState.IDLE.

*/
enum class PlayerState {
    IDLE, PLAYING, PAUSED, STOPPED
}
