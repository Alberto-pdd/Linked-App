package pdalbert.apps.linked.ui.animations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.UUID

@Composable
fun <T> rememberVisibilityTracker(
    currentItems: List<T>,
    idExtractor: (T) -> UUID
): Set<UUID> {
    var previousIds by remember { mutableStateOf<Set<UUID>>(emptySet()) }
    val currentIds = remember(currentItems) {
        derivedStateOf { currentItems.map(idExtractor).toSet() }
    }
    LaunchedEffect(currentIds.value) {
        previousIds = currentIds.value
    }
    return previousIds
}