package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.repository.LinkRepository
import java.util.UUID

class AllLinksViewModel(
    private val linkRepository: LinkRepository
) : ViewModel() {

    val links: StateFlow<List<Link>> = linkRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _activeTag = MutableStateFlow("Todos")
    val activeTag: StateFlow<String> = _activeTag

    val filteredLinks: StateFlow<List<Link>> = combine(links, _searchQuery, _activeTag) { allLinks, query, tag ->
        allLinks.filter { link ->
            val matchesTag = tag == "Todos" || link.tag.equals(tag, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                link.title.contains(query, ignoreCase = true) ||
                link.url.contains(query, ignoreCase = true) ||
                link.tag.contains(query, ignoreCase = true)
            matchesTag && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableTags: StateFlow<List<String>> = combine(links, _activeTag) { allLinks, _ ->
        listOf("Todos") + allLinks.map { it.tag }.filter { it.isNotEmpty() }.distinct()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("Todos"))

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
        _activeTag.value = tag
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
}

class AllLinksViewModelFactory(
    private val linkRepository: LinkRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllLinksViewModel(linkRepository) as T
    }
}
