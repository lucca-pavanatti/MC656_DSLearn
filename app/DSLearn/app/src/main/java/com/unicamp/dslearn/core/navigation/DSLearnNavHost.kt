package com.unicamp.dslearn.core.navigation

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.unicamp.dslearn.core.model.Difficulty
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.presentation.screens.account.AccountScreen
import com.unicamp.dslearn.presentation.screens.account.metrics.MetricsScreen
import com.unicamp.dslearn.presentation.screens.exercisedetail.ExerciseDetailScreen
import com.unicamp.dslearn.presentation.screens.exercises.ExercisesScreen
import com.unicamp.dslearn.presentation.screens.exercises.TopicExercisesScreen
import com.unicamp.dslearn.presentation.screens.exercises.getGradientForIndex
import com.unicamp.dslearn.presentation.screens.home.HomeScreen
import com.unicamp.dslearn.presentation.screens.topicdetail.TopicDetailScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Exercises

@Serializable
object Account

@Serializable
data class TopicDetail(val name: String, val content: String, val isCompleted: Boolean)

@Serializable
data class TopicExercises(val topicName: String)

@Serializable
data class ExerciseDetail(
    val id: Int,
    val title: String,
    val url: String,
    val difficulty: Difficulty,
    val relatedTopics: String,
    val companies: String,
    val completed: Boolean
)

@Serializable
object Metrics

fun NavController.navigateToTopicDetailScreen(name: String, content: String, isCompleted: Boolean) =
    navigate(route = TopicDetail(name, content, isCompleted))

fun NavController.navigateToTopicExercisesScreen(topicName: String) =
    navigate(route = TopicExercises(topicName))

fun NavController.navigateToMetricsScreen() = navigate(route = Metrics)

fun NavController.navigateToExerciseDetailScreen(
    id: Int,
    title: String,
    url: String,
    difficulty: Difficulty,
    relatedTopics: String,
    companies: String,
    completed: Boolean
) =
    navigate(
        route = ExerciseDetail(
            id,
            title,
            url,
            difficulty,
            relatedTopics,
            companies,
            completed
        )
    )

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun DSLearnNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
            .fillMaxSize()
    ) {
        composable<Home> {
            HomeScreen(
                onTopicClick = { name, content, isCompleted ->
                    navController.navigateToTopicDetailScreen(name, content, isCompleted)
                },
            )
        }
        composable<Exercises> {
            ExercisesScreen(
                onExerciseClick = { exerciseModel ->
                    exerciseModel.apply {
                        navController.navigateToExerciseDetailScreen(
                            id,
                            title,
                            url,
                            difficulty,
                            relatedTopics,
                            companies,
                            completed
                        )
                    }
                },
            )
        }
        composable<Account> {
            AccountScreen(
                onMetricsClick = { navController.navigateToMetricsScreen() }
            )
        }
        composable<Metrics> { backStackEntry ->
            MetricsScreen(
                onBack = { navController.navigateUp() }
            )
        }
        composable<TopicDetail>(
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
            val topicDetail: TopicDetail = backStackEntry.toRoute()
            TopicDetailScreen(
                topicName = topicDetail.name,
                topicContent = topicDetail.content,
                isCompleted = topicDetail.isCompleted,
                onGoToExercises = { navController.navigateToTopicExercisesScreen(topicDetail.name) },
                onBack = { navController.navigateUp() },
            )
        }
        composable<ExerciseDetail> { backStackEntry ->
            val exercise: ExerciseDetail = backStackEntry.toRoute()
            ExerciseDetailScreen(
                exercise = ExerciseModel(
                    id = exercise.id,
                    title = exercise.title,
                    url = exercise.url,
                    difficulty = exercise.difficulty,
                    relatedTopics = exercise.relatedTopics,
                    companies = exercise.companies,
                    completed = exercise.completed
                ),
                gradient = getGradientForIndex(exercise.id),
                onBackClick = { navController.navigateUp() },
                onOpenUrl = { context, url ->
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                }
            )
        }
        composable<TopicExercises> { backStackEntry ->
            val topic: TopicExercises = backStackEntry.toRoute()
            TopicExercisesScreen(
                topicName = topic.topicName,
                onBackClick = { navController.navigateUp() },
                onExerciseClick = { exerciseModel ->
                    exerciseModel.apply {
                        navController.navigateToExerciseDetailScreen(
                            id,
                            title,
                            url,
                            difficulty,
                            relatedTopics,
                            companies,
                            completed
                        )
                    }
                },
            )
        }


    }
}

enum class Destination(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    HOME(Home, "Home", Icons.Outlined.Home, "Home"),
    EXERCISES(Exercises, "Exercises", Icons.Outlined.Edit, "Exercises"),
    ACCOUNT(Account, "Account", Icons.Outlined.AccountCircle, "Account");

    companion object {
        fun hasRoute(route: String?): Boolean {
            route ?: return false

            return route in entries.map {
                it.route::class.qualifiedName?.also { msg -> Log.d("hasRoute", msg) }
            }
        }
    }
}






