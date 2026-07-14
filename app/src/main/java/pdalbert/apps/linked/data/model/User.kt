package pdalbert.apps.linked.data.model

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String
) {
    val initials: String
        get() = name.split(" ").take(2).map { it.first().uppercaseChar() }.joinToString("")
}
