package pdalbert.apps.linked.ui.animations

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun <T> rememberStaggerState(
    items: List<T>,
    getId: (T) -> Any,
    staggerDelay: Long = 60L,
    enterDuration: Int = 400,
    maxVisibleItems: Int = 20
): Map<Any, Float> {
    val progressMap = remember { mutableStateMapOf<Any, Float>() }
    var previousItemIds by remember { mutableStateOf(setOf<Any>()) }

    LaunchedEffect(items.map(getId)) {
        val currentIds = items.map(getId).toSet()
        val listChanged = previousItemIds.isNotEmpty() && previousItemIds != currentIds

        if (listChanged) {
            val animatedCount = minOf(items.size, maxVisibleItems)

            items.take(animatedCount).forEachIndexed { index, item ->
                val id = getId(item)
                launch {
                    delay(index * staggerDelay)
                    animate(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = tween(enterDuration)
                    ) { value, _ ->
                        progressMap[id] = value
                    }
                }
            }
        } else {
            val animatedCount = minOf(items.size, maxVisibleItems)

            items.take(animatedCount).forEachIndexed { index, item ->
                val id = getId(item)
                launch {
                    delay(index * staggerDelay)
                    animate(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = tween(enterDuration)
                    ) { value, _ ->
                        progressMap[id] = value
                    }
                }
            }
        }

        previousItemIds = currentIds
    }

    return progressMap
}
