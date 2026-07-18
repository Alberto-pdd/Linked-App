package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.model.User
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

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
