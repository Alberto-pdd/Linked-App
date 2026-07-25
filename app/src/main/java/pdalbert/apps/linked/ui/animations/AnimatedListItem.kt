package pdalbert.apps.linked.ui.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable

@Composable
fun AnimatedListItem(
    isVisible: Boolean,
    index: Int,
    content: @Composable () -> Unit,
    enterDuration: Int = 400,
    exitDuration: Int = 300,
    staggerDelay: Int = 60,
    offsetFraction: Float = 1f / 3f
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { (it * offsetFraction).toInt() },
            animationSpec = tween(enterDuration, delayMillis = index * staggerDelay)
        ) + fadeIn(animationSpec = tween(enterDuration, delayMillis = index * staggerDelay)),
        exit = slideOutVertically(
            targetOffsetY = { (it * offsetFraction).toInt() },
            animationSpec = tween(exitDuration)
        ) + fadeOut(animationSpec = tween(exitDuration))
    ) {
        content()
    }
}