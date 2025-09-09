package com.unicamp.dslearn.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.unicamp.dslearn.presentation.carddetail.CardDetailScreen
import com.unicamp.dslearn.presentation.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class CardDetail(val id: Int, val name: String)

fun NavController.navigateToCardDetailScreen(id: Int, name: String) =
    navigate(route = CardDetail(id, name))


@Composable
fun DSLearnNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable<Home> {
            HomeScreen(onCardClick = { cardId, name ->
                navController.navigateToCardDetailScreen(cardId, name)
            })
        }
        composable<CardDetail>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val cardDetail: CardDetail = backStackEntry.toRoute()
            CardDetailScreen(
                cardId = cardDetail.id,
                cardName = cardDetail.name
            )
        }


    }
}


