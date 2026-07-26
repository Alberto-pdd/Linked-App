package pdalbert.apps.linked.domain.usecase

import pdalbert.apps.linked.data.model.Folder
import javax.inject.Inject

class FilterFoldersUseCase @Inject constructor() {

    fun filter(
        folders: List<Folder>,
        query: String = "",
        sortOption: String = "Más enlaces"
    ): List<Folder> {
        return folders
            .filter { folder ->
                query.isBlank() || folder.name.contains(query, ignoreCase = true)
            }
            .let { filtered ->
                when (sortOption) {
                    "Más antiguo" -> filtered.sortedBy { it.createdAt }
                    "Más enlaces" -> filtered.sortedByDescending { it.name }
                    "Menos enlaces" -> filtered.sortedBy { it.name }
                    else -> filtered.sortedByDescending { it.createdAt }
                }
            }
    }
}
