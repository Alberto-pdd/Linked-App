package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.local.SessionManager
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val isLoggedIn = sessionManager.isLoggedIn()
            delay(SPLASH_DURATION_MS)
            _destination.value = if (isLoggedIn) "home" else "login"
        }
    }

    companion object {
        const val SPLASH_DURATION_MS = 2200L
    }
}
