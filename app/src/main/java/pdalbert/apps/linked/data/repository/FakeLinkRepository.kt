package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import pdalbert.apps.linked.data.model.Link
import java.util.UUID

class FakeLinkRepository : LinkRepository {

    private val links = mutableListOf<Link>()
    private val _links = MutableStateFlow<List<Link>>(emptyList())

    init {
        links.addAll(
            listOf(
                Link(
                    title = "Figma — Collaborative Design",
                    url = "https://figma.com",
                    emoji = "🎨",
                    bgColor = "#F3E8FF",
                    description = "Herramienta de diseño colaborativo",
                    tag = "Diseño"
                ),
                Link(
                    title = "Claude by Anthropic",
                    url = "https://claude.ai",
                    emoji = "🤖",
                    bgColor = "#E0F2FE",
                    description = "Asistente de IA avanzado",
                    tag = "IA"
                ),
                Link(
                    title = "TechCrunch — Latest News",
                    url = "https://techcrunch.com",
                    emoji = "📰",
                    bgColor = "#DCFCE7",
                    description = "Noticias de tecnología",
                    tag = "Noticias"
                ),
                Link(
                    title = "YouTube — Ver después",
                    url = "https://youtube.com",
                    emoji = "🎬",
                    bgColor = "#FEE2E2",
                    description = "Videos guardados para ver",
                    tag = "Video"
                )
            )
        )
        _links.value = links.toList()
    }

    override fun getAll(): Flow<List<Link>> = _links

    override fun getById(id: UUID): Flow<Link?> = _links.map { list ->
        list.find { it.id == id }
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
        _links.value = links.toList()
    }
}
