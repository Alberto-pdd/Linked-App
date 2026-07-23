package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.model.Tag
import pdalbert.apps.linked.data.repository.FolderRepository
import pdalbert.apps.linked.data.repository.LinkRepository
import pdalbert.apps.linked.data.repository.TagRepository
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val linkRepository: LinkRepository,
    private val folderRepository: FolderRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    val links: StateFlow<List<Link>> = linkRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val folders: StateFlow<List<Folder>> = folderRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tags: StateFlow<List<Tag>> = tagRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _activeTags = MutableStateFlow<List<String>>(emptyList())
    val activeTags: StateFlow<List<String>> = _activeTags

    private val _isMultiTagMode = MutableStateFlow(false)
    val isMultiTagMode: StateFlow<Boolean> = _isMultiTagMode

    private val _linkSortAscending = MutableStateFlow(false)
    val linkSortAscending: StateFlow<Boolean> = _linkSortAscending

    private val _linkTimePeriod = MutableStateFlow("Esta semana")
    val linkTimePeriod: StateFlow<String> = _linkTimePeriod

    private val _folderSortOption = MutableStateFlow("Más nuevo")
    val folderSortOption: StateFlow<String> = _folderSortOption

    val filteredLinks: StateFlow<List<Link>> = _activeTags.flatMapLatest { activeTags ->
        if (activeTags.isEmpty()) {
            linkRepository.getAll()
        } else {
            linkRepository.getByTagNames(activeTags)
        }
    }.combine(_searchQuery) { links, query ->
        links.filter { link ->
            query.isBlank() ||
                link.title.contains(query, ignoreCase = true) ||
                link.url.contains(query, ignoreCase = true) ||
                link.description.contains(query, ignoreCase = true)
        }
    }.combine(_linkSortAscending) { links, ascending ->
        if (ascending) links.sortedBy { it.createdAt } else links.sortedByDescending { it.createdAt }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredFolders: StateFlow<List<Folder>> = combine(
        folders, _searchQuery, _activeTags, _folderSortOption
    ) { allFolders, query, activeTags, sortOption ->
        val filtered = allFolders.filter { folder ->
            val matchesSearch = query.isBlank() || folder.name.contains(query, ignoreCase = true)
            val matchesTag = activeTags.isEmpty() || activeTags.any { tag -> 
                folder.name.contains(tag, ignoreCase = true) 
            }
            matchesSearch && matchesTag
        }
        when (sortOption) {
            "Más antiguo" -> filtered.sortedBy { it.createdAt }
            "Más enlaces" -> filtered.sortedByDescending { it.name }
            "Menos enlaces" -> filtered.sortedBy { it.name }
            else -> filtered.sortedByDescending { it.createdAt }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _showAddLinkSheet = MutableStateFlow(false)
    val showAddLinkSheet: StateFlow<Boolean> = _showAddLinkSheet

    private val _showAddFolderSheet = MutableStateFlow(false)
    val showAddFolderSheet: StateFlow<Boolean> = _showAddFolderSheet

    private val _editingLink = MutableStateFlow<Link?>(null)
    val editingLink: StateFlow<Link?> = _editingLink

    private val _editingFolder = MutableStateFlow<Folder?>(null)
    val editingFolder: StateFlow<Folder?> = _editingFolder

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    init {
        viewModelScope.launch {
            sessionManager.getUser()?.let { user ->
                _userName.value = user.name.split(" ").take(2)
                    .map { it.first().uppercaseChar() }.joinToString("")
            }
            _isMultiTagMode.value = sessionManager.getMultiTagMode()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onTagSelected(tag: String) {
        val current = _activeTags.value.toMutableList()
        if (_isMultiTagMode.value) {
            if (tag in current) current.remove(tag) else current.add(tag)
        } else {
            current.clear()
            if (tag !in _activeTags.value) current.add(tag)
        }
        _activeTags.value = current
    }

    fun setMultiTagMode(enabled: Boolean) {
        _isMultiTagMode.value = enabled
        if (!enabled && _activeTags.value.size > 1) {
            _activeTags.value = listOf(_activeTags.value.first())
        }
        viewModelScope.launch {
            sessionManager.setMultiTagMode(enabled)
        }
    }

    fun onLinkTimePeriodChanged(period: String) {
        _linkTimePeriod.value = period
    }

    fun onLinkSortDirectionChanged() {
        _linkSortAscending.value = !_linkSortAscending.value
    }

    fun onFolderSortChanged(sortOption: String) {
        _folderSortOption.value = sortOption
    }

    fun onAvatarClicked() {
        _navigationEvent.value = "settings"
    }

    fun onSeeAllLinksClicked() {
        _navigationEvent.value = "all_links"
    }

    fun onSeeAllFoldersClicked() {
        _navigationEvent.value = "all_folders"
    }

    fun onAddLinkClicked() {
        _editingLink.value = null
        _showAddLinkSheet.value = true
    }

    fun onAddFolderClicked() {
        _editingFolder.value = null
        _showAddFolderSheet.value = true
    }

    fun onEditLinkClicked(link: Link) {
        _editingLink.value = link
        _showAddLinkSheet.value = true
    }

    fun onEditFolderClicked(folder: Folder) {
        _editingFolder.value = folder
        _showAddFolderSheet.value = true
    }

    fun onDeleteLinkClicked(linkId: UUID) {
        viewModelScope.launch {
            linkRepository.delete(linkId)
            _toastMessage.value = "Enlace eliminado"
        }
    }

    fun onDeleteFolderClicked(folderId: UUID) {
        viewModelScope.launch {
            folderRepository.delete(folderId)
            _toastMessage.value = "Carpeta eliminada"
        }
    }

    fun onLinkSaved(link: Link) {
        viewModelScope.launch {
            if (_editingLink.value != null) {
                linkRepository.update(link)
                _toastMessage.value = "Enlace actualizado"
            } else {
                linkRepository.create(link)
                _toastMessage.value = "Enlace añadido"
            }
            _showAddLinkSheet.value = false
            _editingLink.value = null
        }
    }

    fun onFolderSaved(folder: Folder) {
        viewModelScope.launch {
            if (_editingFolder.value != null) {
                folderRepository.update(folder)
                _toastMessage.value = "Carpeta actualizada"
            } else {
                folderRepository.create(folder)
                _toastMessage.value = "Carpeta creada"
            }
            _showAddFolderSheet.value = false
            _editingFolder.value = null
        }
    }

    fun onSheetDismissed() {
        _showAddLinkSheet.value = false
        _showAddFolderSheet.value = false
        _editingLink.value = null
        _editingFolder.value = null
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun getTagsForLink(linkId: java.util.UUID): kotlinx.coroutines.flow.Flow<List<pdalbert.apps.linked.data.model.Tag>> =
        linkRepository.getTagsForLink(linkId)

    fun getTimeAgo(createdAt: kotlinx.datetime.Instant): String {
        val now = kotlinx.datetime.Clock.System.now()
        val duration = now - createdAt
        val seconds = duration.inWholeSeconds
        return when {
            seconds < 60 -> "hace instantes"
            seconds < 3600 -> "hace ${seconds / 60} min"
            seconds < 86400 -> "hace ${seconds / 3600}h"
            seconds < 604800 -> "hace ${seconds / 86400}d"
            else -> "hace ${seconds / 604800}sem"
        }
    }
}
