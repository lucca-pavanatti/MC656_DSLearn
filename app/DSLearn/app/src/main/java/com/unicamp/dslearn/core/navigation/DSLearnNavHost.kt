package com.unicamp.dslearn.core.navigation

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
data class CardDetail(val id: Int)

fun NavController.navigateToCardDetailScreen(id: Int) =
    navigate(route = CardDetail(id))


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
            HomeScreen(onCardClick = { cardId ->
                navController.navigateToCardDetailScreen(cardId)
            })
        }
        composable<CardDetail> { backStackEntry ->
            val cardDetail: CardDetail = backStackEntry.toRoute()
            CardDetailScreen(
                cardId = cardDetail.id,
            )
        }


    }
}


