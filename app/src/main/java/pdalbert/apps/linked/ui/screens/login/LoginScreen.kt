package pdalbert.apps.linked.ui.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import pdalbert.apps.linked.R
import pdalbert.apps.linked.ui.theme.Background
import pdalbert.apps.linked.ui.theme.DMSerifDisplay
import pdalbert.apps.linked.ui.theme.DMSerifDisplayItalic
import pdalbert.apps.linked.ui.theme.Manrope

private val Surface = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextMuted = Color(0xFF999999)
private val Accent = Color(0xFF2563EB)

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo centrado en la parte alta
            Spacer(modifier = Modifier.weight(0.25f))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.linked_logo),
                        contentDescription = "Linked logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = "Linked",
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        color = TextPrimary,
                        letterSpacing = (-0.4).sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.22f))

            // Cards zone
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                // Card 1 - Design Resources (izquierda, rotación -4deg)
                FloatingCard(
                    title = "Design Resources",
                    domain = "dribbble.com",
                    iconRes = R.drawable.dribbble_icon,
                    bgColor = Color(0xFFFDEEE0),
                    rotation = -4f,
                    floatDuration = 3800,
                    floatDelay = 0,
                    modifier = Modifier
                        .padding(start = 28.dp, top = 32.dp)
                        .align(Alignment.TopStart)
                )

                // Card 2 - Tech News (derecha, rotación +4deg)
                FloatingCard(
                    title = "Tech News Daily",
                    domain = "techcrunch.com",
                    iconRes = R.drawable.ic_scroll,
                    bgColor = Color(0xFFE2EEFF),
                    rotation = 4f,
                    floatDuration = 4200,
                    floatDelay = 1500,
                    modifier = Modifier
                        .padding(end = 28.dp, top = 120.dp)
                        .align(Alignment.TopEnd)
                )

                // Card 3 - Watch Later (izquierda, rotación -4deg)
                FloatingCard(
                    title = "Watch Later",
                    domain = "youtube.com",
                    iconRes = R.drawable.youtube_icon,
                    bgColor = Color(0xFFEDE2FF),
                    rotation = -4f,
                    floatDuration = 4000,
                    floatDelay = 750,
                    modifier = Modifier
                        .padding(start = 42.dp, top = 215.dp)
                        .align(Alignment.TopStart)
                )
            }

            Spacer(modifier = Modifier.weight(0.25f))

            // Contenido inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
                    .padding(bottom = 50.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Todos tus\nenlaces, ")
                        withStyle(
                            style = SpanStyle(
                                color = Accent,
                                fontFamily = DMSerifDisplayItalic,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("en orden.")
                        }
                    },
                    fontFamily = DMSerifDisplay,
                    fontWeight = FontWeight.Normal,
                    fontSize = 42.sp,
                    color = TextPrimary,
                    lineHeight = 44.sp,
                    letterSpacing = (-1.5).sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Text(
                    text = "Guarda, organiza y accede a tus enlaces\nfavoritos desde cualquier lugar.",
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xFF777777),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor = Color.Black.copy(alpha = 0.05f)
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(Surface)
                        .border(1.5.dp, Color.Black.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
                        .clickable { viewModel.onGoogleSignInSuccess("Usuario", "test@linked.com") }
                        .padding(vertical = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google",
                            modifier = Modifier.size(25.dp)
                        )
                        Spacer(modifier = Modifier.width(9.dp))
                        Text(
                            text = "Continuar con Google",
                            fontFamily = Manrope,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = TextPrimary,
                            letterSpacing = (-0.2).sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Al continuar, aceptas los ")
                        withStyle(style = SpanStyle(color = Accent)) {
                            append("Términos de uso")
                        }
                        append(" y la ")
                        withStyle(style = SpanStyle(color = Accent)) {
                            append("Política de\nprivacidad")
                        }
                        append(" de Linked.")
                    },
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFFBBBBBB),
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FloatingCard(
    title: String,
    domain: String,
    iconRes: Int,
    bgColor: Color,
    rotation: Float,
    floatDuration: Int,
    floatDelay: Int,
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(floatDelay.toLong())
        while (true) {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = floatDuration / 2,
                    easing = LinearEasing
                )
            )
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = floatDuration / 2,
                    easing = LinearEasing
                )
            )
        }
    }

    val yOffset = (animatable.value - 0.5f) * 16f

    Box(
        modifier = modifier
            .offset(y = yOffset.dp)
            .rotate(rotation)
    ) {
        LoginCard(
            title = title,
            domain = domain,
            iconRes = iconRes,
            bgColor = bgColor
        )
    }
}

@Composable
private fun LoginCard(
    title: String,
    domain: String,
    iconRes: Int,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Surface)
            .border(1.dp, Color.Black.copy(alpha = 0.07f), RoundedCornerShape(16.dp))
            .padding(13.dp, 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    letterSpacing = (-0.2).sp
                )
                Text(
                    text = domain,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
