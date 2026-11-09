package com.example.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class ConfettiShapeType {
    RECTANGLE, CIRCLE, DIAMOND
}

data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var rotation: Float,
    val rotationSpeed: Float,
    val size: Float,
    val color: Color,
    val shape: ConfettiShapeType
)

/**
 * High-performance Jetpack Compose Canvas Confetti particle animation.
 * Triggers realistic physics-based confetti bursts for positive reinforcement upon badge unlocking.
 */
@Composable
fun ConfettiCanvas(
    isTriggered: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 80,
    durationMillis: Int = 2500,
    onAnimationEnd: () -> Unit = {}
) {
    if (!isTriggered) return

    val progress = remember { Animatable(0f) }
    val particles = remember(isTriggered) {
        val colors = listOf(
            Color(0xFFF59E0B), // Gold
            Color(0xFF10B981), // Emerald
            Color(0xFF06B6D4), // Cyan
            Color(0xFF7C3AED), // Purple
            Color(0xFFEC4899), // Pink
            Color(0xFFEF4444), // Coral
            Color(0xFF3B82F6)  // Blue
        )
        val rng = Random(System.currentTimeMillis())
        List(particleCount) {
            val angle = rng.nextFloat() * 2f * Math.PI.toFloat()
            val speed = 4f + rng.nextFloat() * 8f
            ConfettiParticle(
                x = 0f,
                y = 0f,
                vx = cos(angle) * speed,
                vy = sin(angle) * speed - 6f, // Initial upward burst
                rotation = rng.nextFloat() * 360f,
                rotationSpeed = (rng.nextFloat() - 0.5f) * 20f,
                size = 14f + rng.nextFloat() * 16f,
                color = colors[rng.nextInt(colors.size)],
                shape = ConfettiShapeType.values()[rng.nextInt(ConfettiShapeType.values().size)]
            )
        }
    }

    LaunchedEffect(isTriggered) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
        )
        onAnimationEnd()
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag("confetti_canvas")
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val alpha = (1f - progress.value).coerceIn(0f, 1f)
        val gravity = 0.35f
        val drag = 0.96f

        particles.forEach { p ->
            p.x += p.vx
            p.y += p.vy
            p.vx *= drag
            p.vy = (p.vy + gravity) * drag
            p.rotation += p.rotationSpeed

            val currentAlpha = alpha
            val particleColor = p.color.copy(alpha = currentAlpha)

            rotate(p.rotation, pivot = Offset(centerX + p.x, centerY + p.y)) {
                when (p.shape) {
                    ConfettiShapeType.RECTANGLE -> {
                        drawRect(
                            color = particleColor,
                            topLeft = Offset(centerX + p.x - p.size / 2f, centerY + p.y - p.size / 4f),
                            size = Size(p.size, p.size * 0.5f)
                        )
                    }
                    ConfettiShapeType.CIRCLE -> {
                        drawCircle(
                            color = particleColor,
                            radius = p.size * 0.35f,
                            center = Offset(centerX + p.x, centerY + p.y)
                        )
                    }
                    ConfettiShapeType.DIAMOND -> {
                        val path = Path().apply {
                            moveTo(centerX + p.x, centerY + p.y - p.size * 0.6f)
                            lineTo(centerX + p.x + p.size * 0.4f, centerY + p.y)
                            lineTo(centerX + p.x, centerY + p.y + p.size * 0.6f)
                            lineTo(centerX + p.x - p.size * 0.4f, centerY + p.y)
                            close()
                        }
                        drawPath(path = path, color = particleColor)
                    }
                }
            }
        }
    }
}

/**
 * Full-screen celebration dialog with custom Canvas Confetti burst
 * awarded when a student earns a new achievement milestone badge.
 */
@Composable
fun AchievementUnlockedDialog(
    badgeTitle: String,
    badgeCategory: String,
    badgeDescription: String,
    onDismiss: () -> Unit
) {
    var triggerConfetti by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("achievement_dialog_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Badge Trophy",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "وسام إنجاز جديد! 🎉",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = badgeCategory,
                        fontSize = 12.sp,
                        color = Color(0xFF7C3AED),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = badgeTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = badgeDescription,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Color(0xFF10B981).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تم تحقيق المعيار الوزاري بنجاح",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("celebrate_dismiss_button")
                    ) {
                        Text("متابعة التفوق", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Confetti Canvas Particle Overlay
            ConfettiCanvas(
                isTriggered = triggerConfetti,
                modifier = Modifier
                    .matchParentSize()
                    .padding(8.dp),
                onAnimationEnd = { triggerConfetti = false }
            )
        }
    }
}
