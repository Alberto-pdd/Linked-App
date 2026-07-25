package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import pdalbert.apps.linked.data.model.FolderTag
import pdalbert.apps.linked.data.model.LinkTag
import pdalbert.apps.linked.data.model.Tag
import java.util.UUID
import javax.inject.Inject

class FakeTagRepository @Inject constructor() : TagRepository {

    private val tags = mutableListOf<Tag>()
    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    private val linkTags = mutableListOf<LinkTag>()
    private val folderTags = mutableListOf<FolderTag>()

    init {
        tags.addAll(
            listOf(
                Tag(name = "Diseño", colorName = "PURPLE"),
                Tag(name = "IA", colorName = "BLUE"),
                Tag(name = "Noticias", colorName = "GREEN"),
                Tag(name = "Video", colorName = "RED"),
                Tag(name = "Música", colorName = "ORANGE"),
                Tag(name = "Gaming", colorName = "TEAL"),
                Tag(name = "Fotografía", colorName = "PINK")
            )
        )
        _tags.value = tags.toList()
    }

    override fun getAll(): Flow<List<Tag>> = _tags

    override fun getById(id: UUID): Flow<Tag?> = _tags.map { list ->
        list.find { it.id == id }
    }

    override fun getByLinkId(linkId: UUID): Flow<List<Tag>> = _tags.map { list ->
        val tagIds = linkTags.filter { it.linkId == linkId }.map { it.tagId }
        list.filter { it.id in tagIds }
    }

    override fun getByFolderId(folderId: UUID): Flow<List<Tag>> = _tags.map { list ->
        val tagIds = folderTags.filter { it.folderId == folderId }.map { it.tagId }
        list.filter { it.id in tagIds }
    }

    override suspend fun create(tag: Tag) {
        tags.add(tag)
        _tags.value = tags.toList()
    }

    override suspend fun update(tag: Tag) {
        val index = tags.indexOfFirst { it.id == tag.id }
        if (index != -1) {
            tags[index] = tag
            _tags.value = tags.toList()
        }
    }

    override suspend fun delete(id: UUID) {
        tags.removeAll { it.id == id }
        linkTags.removeAll { it.tagId == id }
        folderTags.removeAll { it.tagId == id }
        _tags.value = tags.toList()
    }

    override suspend fun addTagToLink(linkId: UUID, tagId: UUID) {
        if (linkTags.none { it.linkId == linkId && it.tagId == tagId }) {
            linkTags.add(LinkTag(linkId = linkId, tagId = tagId))
        }
    }

    override suspend fun removeTagFromLink(linkId: UUID, tagId: UUID) {
        linkTags.removeAll { it.linkId == linkId && it.tagId == tagId }
    }

    override suspend fun addTagToFolder(folderId: UUID, tagId: UUID) {
        if (folderTags.none { it.folderId == folderId && it.tagId == tagId }) {
            folderTags.add(FolderTag(folderId = folderId, tagId = tagId))
        }
    }

    override suspend fun removeTagFromFolder(folderId: UUID, tagId: UUID) {
        folderTags.removeAll { it.folderId == folderId && it.tagId == tagId }
    }
}
