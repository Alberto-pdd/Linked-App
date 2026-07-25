package pdalbert.apps.linked.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pdalbert.apps.linked.data.model.Tag
import pdalbert.apps.linked.data.model.TagColor
import pdalbert.apps.linked.data.repository.TagRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tagRepository: TagRepository
) : ViewModel() {

    val tags: StateFlow<List<Tag>> = tagRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog

    private val _editingTag = MutableStateFlow<Tag?>(null)
    val editingTag: StateFlow<Tag?> = _editingTag

    private val _deleteTagId = MutableStateFlow<UUID?>(null)
    val deleteTagId: StateFlow<UUID?> = _deleteTagId

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    fun onAddClicked() {
        _editingTag.value = null
        _showAddDialog.value = true
    }

    fun onEditClicked(tag: Tag) {
        _editingTag.value = tag
        _showAddDialog.value = true
    }

    fun onDeleteClicked(tagId: UUID) {
        _deleteTagId.value = tagId
    }

    fun onTagSaved(tag: Tag) {
        viewModelScope.launch {
            if (_editingTag.value != null) {
                tagRepository.update(tag)
                _toastMessage.value = "Etiqueta actualizada"
            } else {
                tagRepository.create(tag)
                _toastMessage.value = "Etiqueta creada"
            }
            _showAddDialog.value = false
            _editingTag.value = null
        }
    }

    fun onDeleteConfirmed() {
        val id = _deleteTagId.value ?: return
        viewModelScope.launch {
            tagRepository.delete(id)
            _toastMessage.value = "Etiqueta eliminada"
            _deleteTagId.value = null
        }
    }

    fun onDeleteCancelled() {
        _deleteTagId.value = null
    }

    fun onDialogDismissed() {
        _showAddDialog.value = false
        _editingTag.value = null
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun onBackClicked() {
        // Handled by NavController
    }
}
