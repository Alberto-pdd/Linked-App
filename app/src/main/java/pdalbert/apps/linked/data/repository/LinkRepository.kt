package pdalbert.apps.linked.data.repository

import kotlinx.coroutines.flow.Flow
import pdalbert.apps.linked.data.model.Link
import java.util.UUID

interface LinkRepository {
    fun getAll(): Flow<List<Link>>
    fun getById(id: UUID): Flow<Link?>
    suspend fun create(link: Link)
    suspend fun update(link: Link)
    suspend fun delete(id: UUID)
}
