package com.hiyako.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiyako.app.data.preferences.SettingsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: SettingsPreferences) : ViewModel() {
    val themeColor = prefs.themeColor.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Red")
    fun setTheme(color: String) = viewModelScope.launch { prefs.setThemeColor(color) }
}
