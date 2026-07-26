package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.model.LinkTag
import pdalbert.apps.linked.data.model.Tag
import java.util.UUID

interface LinkRepository {
    fun getAll(): Flow<List<Link>>
    fun getById(id: UUID): Flow<Link?>
    fun getByFolderId(folderId: UUID): Flow<List<Link>>
    fun getByTagId(tagId: UUID): Flow<List<Link>>
    fun getByTagNames(tagNames: List<String>): Flow<List<Link>>
    fun getTagsForLink(linkId: UUID): Flow<List<Tag>>
    suspend fun create(link: Link)
    suspend fun update(link: Link)
    suspend fun delete(id: UUID)
    suspend fun addTagToLink(linkId: UUID, tagId: UUID)
    suspend fun removeTagFromLink(linkId: UUID, tagId: UUID)
}
