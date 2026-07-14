package pdalbert.apps.linked.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.local.SessionManager

class SplashViewModel(private val sessionManager: SessionManager) : ViewModel() {

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

class SplashViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(sessionManager) as T
    }
}
