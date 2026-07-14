package pdalbert.apps.linked.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pdalbert.apps.linked.data.model.Link
import java.util.UUID

private val Context.linkDataStore: DataStore<Preferences> by preferencesDataStore(name = "linked_links")

class DataStoreLinkRepository(private val context: Context) : LinkRepository {

    private object Keys {
        val LINKS_JSON = stringPreferencesKey("links_json")
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun getAll(): Flow<List<Link>> {
        return context.linkDataStore.data.map { preferences ->
            val jsonString = preferences[Keys.LINKS_JSON] ?: return@map emptyList()
            try {
                json.decodeFromString<List<Link>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override fun getById(id: UUID): Flow<Link?> {
        return getAll().map { links -> links.find { it.id == id } }
    }

    override suspend fun create(link: Link) {
        context.linkDataStore.edit { preferences ->
            val current = try {
                val jsonString = preferences[Keys.LINKS_JSON] ?: "[]"
                json.decodeFromString<List<Link>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
            val updated = current + link
            preferences[Keys.LINKS_JSON] = json.encodeToString(updated)
        }
    }

    override suspend fun update(link: Link) {
        context.linkDataStore.edit { preferences ->
            val current = try {
                val jsonString = preferences[Keys.LINKS_JSON] ?: "[]"
                json.decodeFromString<List<Link>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
            val updated = current.map { if (it.id == link.id) link else it }
            preferences[Keys.LINKS_JSON] = json.encodeToString(updated)
        }
    }

    override suspend fun delete(id: UUID) {
        context.linkDataStore.edit { preferences ->
            val current = try {
                val jsonString = preferences[Keys.LINKS_JSON] ?: "[]"
                json.decodeFromString<List<Link>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
            val updated = current.filter { it.id != id }
            preferences[Keys.LINKS_JSON] = json.encodeToString(updated)
        }
    }
}
