package pdalbert.apps.linked.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pdalbert.apps.linked.data.model.Folder
import java.util.UUID

private val Context.folderDataStore: DataStore<Preferences> by preferencesDataStore(name = "linked_folders")

class DataStoreFolderRepository(private val context: Context) : FolderRepository {

    private object Keys {
        val FOLDERS_JSON = stringPreferencesKey("folders_json")
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun getAll(): Flow<List<Folder>> {
        return context.folderDataStore.data.map { preferences ->
            val jsonString = preferences[Keys.FOLDERS_JSON] ?: return@map emptyList()
            try {
                json.decodeFromString<List<Folder>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override fun getById(id: UUID): Flow<Folder?> {
        return getAll().map { folders -> folders.find { it.id == id } }
    }

    override suspend fun create(folder: Folder) {
        context.folderDataStore.edit { preferences ->
            val current = try {
                val jsonString = preferences[Keys.FOLDERS_JSON] ?: "[]"
                json.decodeFromString<List<Folder>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
            val updated = current + folder
            preferences[Keys.FOLDERS_JSON] = json.encodeToString(updated)
        }
    }

    override suspend fun update(folder: Folder) {
        context.folderDataStore.edit { preferences ->
            val current = try {
                val jsonString = preferences[Keys.FOLDERS_JSON] ?: "[]"
                json.decodeFromString<List<Folder>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
            val updated = current.map { if (it.id == folder.id) folder else it }
            preferences[Keys.FOLDERS_JSON] = json.encodeToString(updated)
        }
    }

    override suspend fun delete(id: UUID) {
        context.folderDataStore.edit { preferences ->
            val current = try {
                val jsonString = preferences[Keys.FOLDERS_JSON] ?: "[]"
                json.decodeFromString<List<Folder>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
            val updated = current.filter { it.id != id }
            preferences[Keys.FOLDERS_JSON] = json.encodeToString(updated)
        }
    }
}
