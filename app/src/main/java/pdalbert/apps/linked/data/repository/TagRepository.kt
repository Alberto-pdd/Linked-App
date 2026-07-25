package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import pdalbert.apps.linked.data.model.Tag
import java.util.UUID

interface TagRepository {
    fun getAll(): Flow<List<Tag>>
    fun getById(id: UUID): Flow<Tag?>
    fun getByLinkId(linkId: UUID): Flow<List<Tag>>
    fun getByFolderId(folderId: UUID): Flow<List<Tag>>
    suspend fun create(tag: Tag)
    suspend fun update(tag: Tag)
    suspend fun delete(id: UUID)
    suspend fun addTagToLink(linkId: UUID, tagId: UUID)
    suspend fun removeTagFromLink(linkId: UUID, tagId: UUID)
    suspend fun addTagToFolder(folderId: UUID, tagId: UUID)
    suspend fun removeTagFromFolder(folderId: UUID, tagId: UUID)
}
