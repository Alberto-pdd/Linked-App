package pdalbert.apps.linked.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.data.repository.FolderRepository
import java.util.UUID

class AllFoldersViewModel(
    private val folderRepository: FolderRepository
) : ViewModel() {

    val folders: StateFlow<List<Folder>> = folderRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredFolders: StateFlow<List<Folder>> = combine(folders, _searchQuery) { allFolders, query ->
        if (query.isBlank()) allFolders
        else allFolders.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showAddSheet = MutableStateFlow(false)
    val showAddSheet: StateFlow<Boolean> = _showAddSheet

    private val _editingFolder = MutableStateFlow<Folder?>(null)
    val editingFolder: StateFlow<Folder?> = _editingFolder

    private val _deleteFolderId = MutableStateFlow<UUID?>(null)
    val deleteFolderId: StateFlow<UUID?> = _deleteFolderId

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onAddClicked() {
        _editingFolder.value = null
        _showAddSheet.value = true
    }

    fun onEditClicked(folder: Folder) {
        _editingFolder.value = folder
        _showAddSheet.value = true
    }

    fun onDeleteClicked(folderId: UUID) {
        _deleteFolderId.value = folderId
    }

    fun onFolderSaved(folder: Folder) {
        viewModelScope.launch {
            if (_editingFolder.value != null) {
                folderRepository.update(folder)
                _toastMessage.value = "\u2713 Carpeta actualizada"
            } else {
                folderRepository.create(folder)
                _toastMessage.value = "\u2713 Carpeta creada"
            }
            _showAddSheet.value = false
            _editingFolder.value = null
        }
    }

    fun onDeleteConfirmed() {
        val id = _deleteFolderId.value ?: return
        viewModelScope.launch {
            folderRepository.delete(id)
            _toastMessage.value = "\uD83D\uDDD1\uFE0F Carpeta eliminada"
            _deleteFolderId.value = null
        }
    }

    fun onDeleteCancelled() {
        _deleteFolderId.value = null
    }

    fun onSheetDismissed() {
        _showAddSheet.value = false
        _editingFolder.value = null
    }

    fun onToastShown() {
        _toastMessage.value = null
    }
}

class AllFoldersViewModelFactory(
    private val folderRepository: FolderRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllFoldersViewModel(folderRepository) as T
    }
}
