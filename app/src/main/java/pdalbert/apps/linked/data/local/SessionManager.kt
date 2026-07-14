package pdalbert.apps.linked.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pdalbert.apps.linked.data.model.User
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "linked_session")

data class SessionData(
    val isLoggedIn: Boolean,
    val userId: String?,
    val userName: String?,
    val userEmail: String?
)

class SessionManager(private val context: Context) {

    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[Keys.IS_LOGGED_IN] ?: false
        }.first()
    }

    suspend fun saveSession(user: User) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_LOGGED_IN] = true
            preferences[Keys.USER_ID] = user.id.toString()
            preferences[Keys.USER_NAME] = user.name
            preferences[Keys.USER_EMAIL] = user.email
        }
    }

    suspend fun getSession(): SessionData? {
        return context.dataStore.data.map { preferences ->
            val isLoggedIn = preferences[Keys.IS_LOGGED_IN] ?: false
            if (!isLoggedIn) return@map null

            SessionData(
                isLoggedIn = true,
                userId = preferences[Keys.USER_ID],
                userName = preferences[Keys.USER_NAME],
                userEmail = preferences[Keys.USER_EMAIL]
            )
        }.first()
    }

    suspend fun getUser(): User? {
        val session = getSession() ?: return null
        return User(
            id = UUID.fromString(session.userId),
            name = session.userName ?: "",
            email = session.userEmail ?: ""
        )
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
