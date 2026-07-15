package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.model.User

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun onGoogleSignInSuccess(name: String, email: String) {
        viewModelScope.launch {
            val user = User(name = name, email = email)
            sessionManager.saveSession(user)
            _navigationEvent.value = "home"
        }
    }

    fun onGoogleSignInError(error: String) {
        // Handle error (show toast, etc.)
    }
}

class LoginViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(sessionManager) as T
    }
}
