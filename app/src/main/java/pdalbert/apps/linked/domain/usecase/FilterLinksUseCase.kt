package pdalbert.apps.linked.domain.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import pdalbert.apps.linked.data.model.Link
import javax.inject.Inject

class FilterLinksUseCase @Inject constructor() {

    fun filter(
        links: List<Link>,
        query: String = "",
        activeTags: List<String> = emptyList(),
        timePeriod: String = "Esta semana",
        sortAscending: Boolean = false
    ): List<Link> {
        return links
            .filter { link ->
                activeTags.isEmpty() || activeTags.any { tag ->
                    link.title.contains(tag, ignoreCase = true)
                }
            }
            .filter { link ->
                query.isBlank() ||
                    link.title.contains(query, ignoreCase = true) ||
                    link.url.contains(query, ignoreCase = true) ||
                    link.description.contains(query, ignoreCase = true)
            }
            .filter { link ->
                val now = Clock.System.now()
                when (timePeriod) {
                    "Hoy" -> link.createdAt >= now.minus(24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                    "Esta semana" -> link.createdAt >= now.minus(7 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                    "Este mes" -> link.createdAt >= now.minus(30 * 24 * 3600 * 1000L, DateTimeUnit.MILLISECOND)
                    else -> true
                }
            }
            .let { filtered ->
                if (sortAscending) filtered.sortedBy { it.createdAt }
                else filtered.sortedByDescending { it.createdAt }
            }
    }
}
