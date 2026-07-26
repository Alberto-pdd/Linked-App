package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.data.model.Link
import java.util.UUID

interface FolderRepository {
    fun getAll(): Flow<List<Folder>>
    fun getById(id: UUID): Flow<Folder?>
    fun getLinksForFolder(folderId: UUID): Flow<List<Link>>
    fun getLinkCountForFolder(folderId: UUID): Flow<Int>
    suspend fun create(folder: Folder)
    suspend fun update(folder: Folder)
    suspend fun delete(id: UUID)
    suspend fun addLinkToFolder(folderId: UUID, linkId: UUID)
    suspend fun removeLinkFromFolder(folderId: UUID, linkId: UUID)
    suspend fun toggleFavorite(folderId: UUID)
}
