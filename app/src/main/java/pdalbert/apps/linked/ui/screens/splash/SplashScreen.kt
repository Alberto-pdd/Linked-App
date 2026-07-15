package pdalbert.apps.linked.ui.screens.splash

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import pdalbert.apps.linked.R
import pdalbert.apps.linked.ui.navigation.Screen
import pdalbert.apps.linked.ui.theme.Accent
import pdalbert.apps.linked.ui.theme.AccentBg
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.Border
import pdalbert.apps.linked.ui.theme.Ink
import pdalbert.apps.linked.ui.theme.InkMuted
import pdalbert.apps.linked.ui.theme.Manrope
import pdalbert.apps.linked.viewmodel.SplashViewModel
import pdalbert.apps.linked.ui.theme.Surface

private data class DecorativeCard(
    val title: String,
    val url: String,
    val iconRes: Int,
    val bgColor: Color
)

private val decorativeCards = listOf(
    DecorativeCard("Figma", "figma.com", R.drawable.figma_icon, Color(0xFFFEF3C7)),
    DecorativeCard("Claude", "claude.ai", R.drawable.claude_icon, Color(0xFFDCFCE7)),
    DecorativeCard("Instagram", "instagram.com", R.drawable.instagram_icon, Color(0xFFEDE9FE)),
    DecorativeCard("YouTube", "youtube.com", R.drawable.youtube_icon, Color(0xFFEBF3FB)),
    DecorativeCard("Dribbble", "dribbble.com", R.drawable.dribbble_icon, Color(0xFFFEF3C7)),
    DecorativeCard("GitHub", "github.com", R.drawable.github_icon, Color(0xFFDCFCE7)),
    DecorativeCard("Pinterest", "pinterest.com", R.drawable.pinterest_icon, Color(0xFFEDE9FE)),
    DecorativeCard("Airbnb", "airbnb.com", R.drawable.airbnb_icon, Color(0xFFEBF3FB)),
    DecorativeCard("LinkedIn", "linkedin.com", R.drawable.linkedin_icon, Color(0xFFFEF3C7)),
    DecorativeCard("Notion", "notion.so", R.drawable.notion_icon, Color(0xFFDCFCE7)),
    DecorativeCard("Telegram", "telegram.org", R.drawable.telegram_icon, Color(0xFFEDE9FE)),
    DecorativeCard("Amazon", "amazon.com", R.drawable.amazon_icon, Color(0xFFEBF3FB)),
)

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel
) {
    var animationPhase by remember { mutableStateOf(0) }
    var logoAlpha by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        animationPhase = 1

        // Cards caen durante 400ms
        kotlinx.coroutines.delay(400)
        animationPhase = 2

        // Fade in gradual del logo (400ms)
        val logoSteps = 16
        val logoDuration = 25L
        for (i in 0..logoSteps) {
            logoAlpha = i.toFloat() / logoSteps
            kotlinx.coroutines.delay(logoDuration)
        }
        logoAlpha = 1f

        // Logo visible 900ms más
        kotlinx.coroutines.delay(900)
        animationPhase = 3
    }

    LaunchedEffect(viewModel.destination) {
        viewModel.destination.collect { dest ->
            dest?.let {
                navController.navigate(it) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        DecorativeCardsLayer(
            visible = animationPhase >= 1,
            modifier = Modifier.fillMaxSize()
        )

        // Radial gradient centrado en el logo (desde el inicio)
        if (animationPhase >= 1) {
            val density = LocalDensity.current
            val config = LocalConfiguration.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Background.copy(alpha = 0.7f),
                                Background.copy(alpha = 0.0f)
                            ),
                            center = Offset(
                                x = density.run { (config.screenWidthDp.dp / 2).toPx() },
                                y = density.run { (config.screenHeightDp.dp / 2).toPx() }
                            ),
                            radius = density.run { 250.dp.toPx() }
                        )
                    )
                    .zIndex(1f)
            )
        }

        if (animationPhase >= 2) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(logoAlpha)
                    .zIndex(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo simple sin card
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.linked_logo),
                        contentDescription = "Linked logo",
                        modifier = Modifier.size(43.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Linked",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 38.sp,
                        color = Ink,
                        letterSpacing = (-1.2).sp
                    )
                }

                // Indicador de carga horizontal
                Spacer(modifier = Modifier.height(24.dp))

                LinearProgressIndicator(
                    modifier = Modifier
                        .width(56.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(100.dp)),
                    color = Accent,
                    trackColor = Border
                )
            }
        }
    }
}

@Composable
private fun DecorativeCardsLayer(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    // Posiciones originales del diseño HTML (pantalla 390x844)
    val originalYPositions = listOf(80, 100, 176, 240, 300, 360, 440, 490, 560, 610, 690, 740)
    val originalXPositions = listOf(18, 210, 40, 190, 35, 205, 55, 195, 30, 200, 48, 190)
    val originalScreenWidth = 390f
    val originalScreenHeight = 844f

    BoxWithConstraints(modifier = modifier) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val scaleX = screenWidth.value / originalScreenWidth
        val scaleY = screenHeight.value / originalScreenHeight

        decorativeCards.forEachIndexed { index, card ->
            var originalX = originalXPositions.getOrElse(index) { 100 }
            val originalY = originalYPositions.getOrElse(index) { 400 }

            // Añadir offset extra a las cards de la derecha
            if (index % 2 == 1) {
                originalX = (originalX + 35f).toInt()
            }

            val xPos = (originalX * scaleX).dp
            val yPos = (originalY * scaleY).dp

            AnimatedCard(
                card = card,
                delayMillis = index * 120,
                modifier = Modifier
                    .offset(x = xPos, y = yPos)
                    .zIndex(0f)
            )
        }
    }
}

@Composable
private fun AnimatedCard(
    card: DecorativeCard,
    delayMillis: Int,
    modifier: Modifier = Modifier
) {
    var alpha by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        val steps = 30
        val durationPerStep = 20L
        for (i in 0..steps) {
            alpha = i.toFloat() / steps
            kotlinx.coroutines.delay(durationPerStep)
        }
        alpha = 1f
    }

    if (alpha == 0f) return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Surface.copy(alpha = alpha))
            .padding(12.dp, 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(card.bgColor.copy(alpha = alpha)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = card.iconRes),
                    contentDescription = card.title,
                    modifier = Modifier.size(22.dp),
                    alpha = alpha,
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = card.title,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Ink.copy(alpha = alpha),
                    letterSpacing = (-0.2).sp
                )
                Text(
                    text = card.url,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = InkMuted.copy(alpha = alpha)
                )
            }
        }
    }
}