package com.example.examenmultimedia.Model


/*Por qué esto es necesario?

Si la enumeración está mal definida o no es accesible desde MediaViewModel, Android Studio marcará el error en PlayerState.IDLE.

*/
enum class PlayerState {
    IDLE, PLAYING, PAUSED, STOPPED
}
