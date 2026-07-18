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
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.data.model.Link
import pdalbert.apps.linked.data.repository.FolderRepository
import pdalbert.apps.linked.data.repository.LinkRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val linkRepository: LinkRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    val links: StateFlow<List<Link>> = linkRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val folders: StateFlow<List<Folder>> = folderRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredLinks: StateFlow<List<Link>> = combine(links, _searchQuery) { allLinks, query ->
        if (query.isBlank()) allLinks
        else allLinks.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.url.contains(query, ignoreCase = true) ||
            it.tag.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredFolders: StateFlow<List<Folder>> = combine(folders, _searchQuery) { allFolders, query ->
        if (query.isBlank()) allFolders
        else allFolders.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    init {
        viewModelScope.launch {
            sessionManager.getUser()?.let { user ->
                _userName.value = user.name.split(" ").take(2).map { it.first().uppercaseChar() }.joinToString("")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
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

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
}
