package com.inkstick.agg5.screens.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditViewModel : ViewModel() {
    // Чтобы пережить поворот экрана
    var name: String = ""
    var code: String = ""
}