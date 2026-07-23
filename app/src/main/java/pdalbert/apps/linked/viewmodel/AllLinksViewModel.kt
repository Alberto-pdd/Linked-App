package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.model.Tag
import pdalbert.apps.linked.data.repository.LinkRepository
import pdalbert.apps.linked.data.repository.TagRepository
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AllLinksViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    val links: StateFlow<List<Link>> = linkRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tags: StateFlow<List<Tag>> = tagRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _activeTags = MutableStateFlow<List<String>>(emptyList())
    val activeTags: StateFlow<List<String>> = _activeTags

    val filteredLinks: StateFlow<List<Link>> = combine(links, _searchQuery, _activeTags) { allLinks, query, activeTags ->
        allLinks.filter { link ->
            val matchesTag = activeTags.isEmpty() || activeTags.any { tag -> 
                link.title.contains(tag, ignoreCase = true) 
            }
            val matchesQuery = query.isBlank() ||
                link.title.contains(query, ignoreCase = true) ||
                link.url.contains(query, ignoreCase = true) ||
                link.description.contains(query, ignoreCase = true)
            matchesTag && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showAddSheet = MutableStateFlow(false)
    val showAddSheet: StateFlow<Boolean> = _showAddSheet

    private val _editingLink = MutableStateFlow<Link?>(null)
    val editingLink: StateFlow<Link?> = _editingLink

    private val _deleteLinkId = MutableStateFlow<UUID?>(null)
    val deleteLinkId: StateFlow<UUID?> = _deleteLinkId

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onTagSelected(tag: String) {
        val current = _activeTags.value.toMutableList()
        if (tag in current) {
            current.remove(tag)
        } else {
            current.add(tag)
        }
        _activeTags.value = current
    }

    fun onAddClicked() {
        _editingLink.value = null
        _showAddSheet.value = true
    }

    fun onEditClicked(link: Link) {
        _editingLink.value = link
        _showAddSheet.value = true
    }

    fun onDeleteClicked(linkId: UUID) {
        _deleteLinkId.value = linkId
    }

    fun onLinkSaved(link: Link) {
        viewModelScope.launch {
            if (_editingLink.value != null) {
                linkRepository.update(link)
                _toastMessage.value = "\u2713 Enlace actualizado"
            } else {
                linkRepository.create(link)
                _toastMessage.value = "\u2713 Enlace añadido"
            }
            _showAddSheet.value = false
            _editingLink.value = null
        }
    }

    fun onDeleteConfirmed() {
        val id = _deleteLinkId.value ?: return
        viewModelScope.launch {
            linkRepository.delete(id)
            _toastMessage.value = "\uD83D\uDDD1\uFE0F Enlace eliminado"
            _deleteLinkId.value = null
        }
    }

    fun onDeleteCancelled() {
        _deleteLinkId.value = null
    }

    fun onSheetDismissed() {
        _showAddSheet.value = false
        _editingLink.value = null
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun onBackClicked() {
        // Handled by NavController
    }

    fun getTagsForLink(linkId: UUID): kotlinx.coroutines.flow.Flow<List<pdalbert.apps.linked.data.model.Tag>> =
        linkRepository.getTagsForLink(linkId)

    fun getTimeAgo(createdAt: Instant): String {
        val now = kotlinx.datetime.Clock.System.now()
        val duration = now - createdAt

        val seconds = duration.inWholeSeconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            years > 0 -> if (years == 1L) "hace 1 año" else "hace $years años"
            months > 0 -> if (months == 1L) "hace 1 mes" else "hace $months meses"
            weeks > 0 -> if (weeks == 1L) "hace 1 semana" else "hace $weeks semanas"
            days > 0 -> if (days == 1L) "hace 1 día" else "hace $days días"
            hours > 0 -> if (hours == 1L) "hace 1 hora" else "hace $hours horas"
            minutes > 0 -> if (minutes == 1L) "hace 1 minuto" else "hace $minutes minutos"
            else -> "ahora mismo"
        }
    }
}
