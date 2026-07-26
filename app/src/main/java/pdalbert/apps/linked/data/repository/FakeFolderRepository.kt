package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import pdalbert.apps.linked.data.model.Folder
import pdalbert.apps.linked.data.model.FolderLink
import pdalbert.apps.linked.data.model.Link
import java.util.UUID
import javax.inject.Inject

class FakeFolderRepository @Inject constructor() : FolderRepository {

    private val folders = mutableListOf<Folder>()
    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    private val folderLinks = mutableListOf<FolderLink>()
    private val links = mutableListOf<Link>()

    init {
        folders.addAll(
            listOf(
                Folder(
                    name = "Trabajo",
                    emoji = "\uD83D\uDCBC",
                    bgColor = "#FEF3C7",
                    isFavorite = true
                ),
                Folder(
                    name = "Recursos Dev",
                    emoji = "\uD83D\uDCBB",
                    bgColor = "#DBEAFE",
                    isFavorite = true
                ),
                Folder(
                    name = "Personal",
                    emoji = "\uD83C\uDFE0",
                    bgColor = "#C8E6C9"
                ),
                Folder(
                    name = "Educación",
                    emoji = "\uD83D\uDCDA",
                    bgColor = "#D1C4E9"
                ),
                Folder(
                    name = "Viajes",
                    emoji = "\u2708\uFE0F",
                    bgColor = "#B3E5FC"
                ),
                Folder(
                    name = "Finanzas",
                    emoji = "\uD83D\uDCB0",
                    bgColor = "#DCEDC8"
                ),
                Folder(
                    name = "Salud",
                    emoji = "\uD83C\uDFE5",
                    bgColor = "#FFCDD2"
                ),
                Folder(
                    name = "Recetas",
                    emoji = "\uD83C\uDF5D",
                    bgColor = "#FFE0B2"
                ),
                Folder(
                    name = "Proyectos",
                    emoji = "\uD83D\uDEE0\uFE0F",
                    bgColor = "#B2DFDB"
                ),
                Folder(
                    name = "Ideas",
                    emoji = "\uD83D\uDCA1",
                    bgColor = "#FFF9C4"
                ),
                Folder(
                    name = "Música",
                    emoji = "\uD83C\uDFB5",
                    bgColor = "#E1BEE7"
                ),
                Folder(
                    name = "Películas",
                    emoji = "\uD83C\uDFAC",
                    bgColor = "#F8BBD0"
                ),
                Folder(
                    name = "Lecturas",
                    emoji = "\uD83D\uDCD6",
                    bgColor = "#D7CCC8"
                ),
                Folder(
                    name = "Gaming",
                    emoji = "\uD83C\uDFAE",
                    bgColor = "#C5CAE9"
                ),
                Folder(
                    name = "Ciencia",
                    emoji = "\uD83D\uDD2C",
                    bgColor = "#B2EBF2"
                ),
                Folder(
                    name = "Fotografía",
                    emoji = "\uD83D\uDCF7",
                    bgColor = "#CFD8DC"
                ),
                Folder(
                    name = "Favoritos",
                    emoji = "\u2B50",
                    bgColor = "#FFCCBC"
                )
            )
        )
        _folders.value = folders.toList()
    }

    override fun getAll(): Flow<List<Folder>> = _folders

    override fun getById(id: UUID): Flow<Folder?> = _folders.map { list ->
        list.find { it.id == id }
    }

    override fun getLinksForFolder(folderId: UUID): Flow<List<Link>> = _folders.map { _ ->
        val linkIds = folderLinks.filter { it.folderId == folderId }.map { it.linkId }
        links.filter { it.id in linkIds }
    }

    override fun getLinkCountForFolder(folderId: UUID): Flow<Int> = _folders.map { _ ->
        folderLinks.count { it.folderId == folderId }
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
        folderLinks.removeAll { it.folderId == id }
        _folders.value = folders.toList()
    }

    override suspend fun addLinkToFolder(folderId: UUID, linkId: UUID) {
        if (folderLinks.none { it.folderId == folderId && it.linkId == linkId }) {
            folderLinks.add(FolderLink(folderId = folderId, linkId = linkId))
        }
    }

    override suspend fun removeLinkFromFolder(folderId: UUID, linkId: UUID) {
        folderLinks.removeAll { it.folderId == folderId && it.linkId == linkId }
    }

    override suspend fun toggleFavorite(folderId: UUID) {
        val index = folders.indexOfFirst { it.id == folderId }
        if (index != -1) {
            val folder = folders[index]
            folders[index] = folder.copy(isFavorite = !folder.isFavorite)
            _folders.value = folders.toList()
        }
    }
}
