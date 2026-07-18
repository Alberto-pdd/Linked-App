package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import pdalbert.apps.linked.data.model.Folder
import java.util.UUID
import javax.inject.Inject

class FakeFolderRepository @Inject constructor() : FolderRepository {

    private val folders = mutableListOf<Folder>()
    private val _folders = MutableStateFlow<List<Folder>>(emptyList())

    init {
        folders.addAll(
            listOf(
                Folder(
                    name = "Trabajo",
                    emoji = "💼",
                    bgColor = "#FEF3C7"
                ),
                Folder(
                    name = "Recursos Dev",
                    emoji = "💻",
                    bgColor = "#DBEAFE"
                )
            )
        )
        _folders.value = folders.toList()
    }

    override fun getAll(): Flow<List<Folder>> = _folders

    override fun getById(id: UUID): Flow<Folder?> = _folders.map { list ->
        list.find { it.id == id }
    }

    override suspend fun create(folder: Folder) {
        folders.add(folder)
        _folders.value = folders.toList()
    }

    override suspend fun update(folder: Folder) {
        val index = folders.indexOfFirst { it.id == folder.id }
        if (index != -1) {
            folders[index] = folder
            _folders.value = folders.toList()
        }
    }

    override suspend fun delete(id: UUID) {
        folders.removeAll { it.id == id }
        _folders.value = folders.toList()
    }
}
