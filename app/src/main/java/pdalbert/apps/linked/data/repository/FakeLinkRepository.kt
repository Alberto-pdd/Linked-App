package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.model.LinkTag
import pdalbert.apps.linked.data.model.Tag
import java.util.UUID
import javax.inject.Inject

class FakeLinkRepository @Inject constructor() : LinkRepository {

    private val links = mutableListOf<Link>()
    private val _links = MutableStateFlow<List<Link>>(emptyList())
    private val linkTags = mutableListOf<LinkTag>()
    private val tags = mutableListOf<Tag>()

    init {
        val now = Clock.System.now()
        tags.addAll(
            listOf(
                Tag(name = "Diseño", colorName = "PURPLE"),
                Tag(name = "IA", colorName = "BLUE"),
                Tag(name = "Noticias", colorName = "GREEN"),
                Tag(name = "Video", colorName = "RED")
            )
        )
        links.addAll(
            listOf(
                Link(
                    title = "Figma — Collaborative Design",
                    url = "https://figma.com",
                    emoji = "🎨",
                    bgColor = "#F3E8FF",
                    description = "Herramienta de diseño colaborativo",
                    createdAt = now.minus(2 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(2 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Claude by Anthropic",
                    url = "https://claude.ai",
                    emoji = "🤖",
                    bgColor = "#E0F2FE",
                    description = "Asistente de IA avanzado",
                    createdAt = now.minus(5 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(5 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "TechCrunch — Latest News",
                    url = "https://techcrunch.com",
                    emoji = "📰",
                    bgColor = "#DCFCE7",
                    description = "Noticias de tecnología",
                    createdAt = now.minus(24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "YouTube — Ver después",
                    url = "https://youtube.com",
                    emoji = "🎬",
                    bgColor = "#FEE2E2",
                    description = "Videos guardados para ver",
                    createdAt = now.minus(2 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(2 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "GitHub",
                    url = "https://github.com",
                    emoji = "🐙",
                    bgColor = "#F0F0F0",
                    description = "Plataforma de desarrollo colaborativo",
                    createdAt = now.minus(3 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(3 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "ChatGPT",
                    url = "https://chat.openai.com",
                    emoji = "🤖",
                    bgColor = "#E8F5E9",
                    description = "Asistente de IA conversacional",
                    createdAt = now.minus(5 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(5 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "The Verge",
                    url = "https://theverge.com",
                    emoji = "📱",
                    bgColor = "#FFF3E0",
                    description = "Noticias de tecnología y ciencia",
                    createdAt = now.minus(7 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(7 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Netflix",
                    url = "https://netflix.com",
                    emoji = "🎬",
                    bgColor = "#FFEBEE",
                    description = "Plataforma de streaming de video",
                    createdAt = now.minus(14 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(14 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Dribbble",
                    url = "https://dribbble.com",
                    emoji = "🏀",
                    bgColor = "#FCE4EC",
                    description = "Comunidad de diseño y creatividad",
                    createdAt = now.minus(21 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(21 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Stack Overflow",
                    url = "https://stackoverflow.com",
                    emoji = "📚",
                    bgColor = "#E3F2FD",
                    description = "Preguntas y respuestas de programación",
                    createdAt = now.minus(30 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(30 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "BBC News",
                    url = "https://bbc.com/news",
                    emoji = "🌍",
                    bgColor = "#ECEFF1",
                    description = "Noticias internacionales",
                    createdAt = now.minus(60 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(60 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Vimeo",
                    url = "https://vimeo.com",
                    emoji = "🎥",
                    bgColor = "#E0F7FA",
                    description = "Plataforma de video para creadores",
                    createdAt = now.minus(90 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(90 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Behance",
                    url = "https://behance.net",
                    emoji = "🎨",
                    bgColor = "#F3E5F5",
                    description = "Portfolio de diseño y arte",
                    createdAt = now.minus(180 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(180 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                ),
                Link(
                    title = "Hugging Face",
                    url = "https://huggingface.co",
                    emoji = "🤗",
                    bgColor = "#FFF8E1",
                    description = "Modelos de IA y machine learning",
                    createdAt = now.minus(365 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND),
                    modifiedAt = now.minus(365 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                )
            )
        )
        _links.value = links.toList()

        runBlocking {
            addTagToLink(links[0].id, tags[0].id) // Figma → Diseño
            addTagToLink(links[1].id, tags[1].id) // Claude → IA
            addTagToLink(links[2].id, tags[2].id) // TechCrunch → Noticias
            addTagToLink(links[3].id, tags[3].id) // YouTube → Video
            addTagToLink(links[4].id, tags[0].id) // GitHub → Diseño
            addTagToLink(links[5].id, tags[1].id) // ChatGPT → IA
            addTagToLink(links[6].id, tags[2].id) // The Verge → Noticias
            addTagToLink(links[7].id, tags[3].id) // Netflix → Video
            addTagToLink(links[8].id, tags[0].id) // Dribbble → Diseño
            addTagToLink(links[9].id, tags[1].id) // Stack Overflow → IA
            addTagToLink(links[10].id, tags[2].id) // BBC News → Noticias
            addTagToLink(links[11].id, tags[3].id) // Vimeo → Video
            addTagToLink(links[12].id, tags[0].id) // Behance → Diseño
            addTagToLink(links[13].id, tags[1].id) // Hugging Face → IA

            // Additional tags for testing "+N" indicator
            addTagToLink(links[0].id, tags[1].id) // Figma → additionally IA
            addTagToLink(links[1].id, tags[0].id) // Claude → additionally Diseño
            addTagToLink(links[4].id, tags[1].id) // GitHub → additionally IA
        }
    }

    override fun getAll(): Flow<List<Link>> = _links

    override fun getById(id: UUID): Flow<Link?> = _links.map { list ->
        list.find { it.id == id }
    }

    override fun getByFolderId(folderId: UUID): Flow<List<Link>> = _links.map { list ->
        list.filter { link ->
            linkTags.any { it.linkId == link.id && it.tagId == folderId }
        }
    }

    override fun getByTagId(tagId: UUID): Flow<List<Link>> = _links.map { list ->
        list.filter { link ->
            linkTags.any { it.linkId == link.id && it.tagId == tagId }
        }
    }

    override fun getTagsForLink(linkId: UUID): Flow<List<Tag>> = _links.map { _ ->
        val tagIds = linkTags.filter { it.linkId == linkId }.map { it.tagId }
        tags.filter { it.id in tagIds }
    }

    override fun getByTagNames(tagNames: List<String>): Flow<List<Link>> = _links.map { list ->
        val matchingTagIds = tags.filter { it.name in tagNames }.map { it.id }
        list.filter { link ->
            linkTags.any { it.linkId == link.id && it.tagId in matchingTagIds }
        }
    }

    override suspend fun create(link: Link) {
        links.add(link)
        _links.value = links.toList()
    }

    override suspend fun update(link: Link) {
        val index = links.indexOfFirst { it.id == link.id }
        if (index != -1) {
            links[index] = link
            _links.value = links.toList()
        }
    }

    override suspend fun delete(id: UUID) {
        links.removeAll { it.id == id }
        linkTags.removeAll { it.linkId == id }
        _links.value = links.toList()
    }

    override suspend fun addTagToLink(linkId: UUID, tagId: UUID) {
        if (linkTags.none { it.linkId == linkId && it.tagId == tagId }) {
            linkTags.add(LinkTag(linkId = linkId, tagId = tagId))
        }
    }

    override suspend fun removeTagFromLink(linkId: UUID, tagId: UUID) {
        linkTags.removeAll { it.linkId == linkId && it.tagId == tagId }
    }
}
