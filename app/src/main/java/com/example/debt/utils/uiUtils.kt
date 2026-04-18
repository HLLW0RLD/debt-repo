package com.example.debt.utils

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

class DateVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val cleanDigits = text.text.filter { it.isDigit() }.take(8)

        val formatted = buildString {
            for (i in cleanDigits.indices) {
                append(cleanDigits[i])
                if ((i == 1 || i == 3) && i != cleanDigits.lastIndex) {
                    append(".")
                }
            }
        }

        val offsetTranslator = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 4 -> offset + 1
                    else -> offset + 2
                }.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }.coerceAtLeast(0)
            }
        }

        return TransformedText(
            AnnotatedString(formatted),
            offsetTranslator
        )
    }
}

inline fun <reified T : Any> NavGraphBuilder.animatedComposable(
    navController: NavController,
    crossinline content: @Composable (T) -> Unit
) {
    composable<T>(
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() }
    ) { backStackEntry ->

        BackHandler {
            navController.popBackStack()
        }
        val screen = backStackEntry.toRoute<T>()
        content(screen)
    }
}

fun enterTransition(): EnterTransition {
    return slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
}

fun exitTransition(): ExitTransition {
    return ExitTransition.None
}

fun popEnterTransition(): EnterTransition {
    return EnterTransition.None
}

fun popExitTransition(): ExitTransition {
    return slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
}