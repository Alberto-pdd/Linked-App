package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import pdalbert.apps.linked.data.model.Folder
import java.util.UUID

interface FolderRepository {
    fun getAll(): Flow<List<Folder>>
    fun getById(id: UUID): Flow<Folder?>
    suspend fun create(folder: Folder)
    suspend fun update(folder: Folder)
    suspend fun delete(id: UUID)
}
