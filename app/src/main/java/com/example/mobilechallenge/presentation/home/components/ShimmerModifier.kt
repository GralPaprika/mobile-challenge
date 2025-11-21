package com.example.mobilechallenge.presentation.home.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.mobilechallenge.ui.theme.Primary
import com.example.mobilechallenge.ui.theme.Secondary
import com.example.mobilechallenge.ui.theme.Tertiary

@Composable
fun Modifier.shimmerBackground(): Modifier {
    val shimmerColors = listOf(
        Primary.copy(alpha = 0.3f),
        Secondary.copy(alpha = 0.5f),
        Tertiary.copy(alpha = 0.3f),
    )

    val transition = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        transition.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = androidx.compose.animation.core.RepeatMode.Restart
            )
        )
    }

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(0f, 0f),
        end = Offset(400f * transition.value, 400f * transition.value)
    )

    return this.background(brush = brush)
}
