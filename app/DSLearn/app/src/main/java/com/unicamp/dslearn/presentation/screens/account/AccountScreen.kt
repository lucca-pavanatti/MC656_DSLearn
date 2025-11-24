package com.unicamp.dslearn.presentation.screens.account

import android.content.Context
import android.credentials.GetCredentialException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.unicamp.dslearn.R
import com.unicamp.dslearn.presentation.screens.account.uistate.AccountUiEvent
import com.unicamp.dslearn.presentation.screens.account.uistate.AccountUiState
import com.unicamp.dslearn.presentation.screens.account.uistate.SignInErrorType
import com.unicamp.dslearn.presentation.screens.account.uistate.SignInResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.security.SecureRandom
import java.util.Base64


private const val TAG = "AccountScreen"
private const val WEB_CLIENT_ID = "web_client_id" // Change to Google Cloud WebClientId

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(viewModel: AccountViewModel = koinViewModel()) {
    val context = LocalContext.current
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AccountUiEvent.SignInSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "Sign In Success", duration = SnackbarDuration.Short
                    )
                }

                is AccountUiEvent.SignInCancelled -> {
                }

                is AccountUiEvent.SignInError -> {
                    val errorMessage = context.getString(
                        when (event.errorType) {
                            SignInErrorType.NO_ACCOUNT -> R.string.error_no_account
                            SignInErrorType.NETWORK_ERROR -> R.string.error_network
                            SignInErrorType.TIMEOUT -> R.string.error_timeout
                            SignInErrorType.PARSING_ERROR -> R.string.error_parsing
                            SignInErrorType.AUTHENTICATION_FAILED -> R.string.error_authentication
                            SignInErrorType.UNKNOWN -> R.string.error_unknown
                        }
                    )

                    snackbarHostState.showSnackbar(
                        message = errorMessage, duration = SnackbarDuration.Long
                    )
                }

                is AccountUiEvent.SignOutSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.sign_out_success),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    val onSignInClick = {
        viewModel.setLoading()

        val googleIdOption = GetSignInWithGoogleOption.Builder(WEB_CLIENT_ID)
            .setNonce(generateSecureRandomNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            val result = signInWithGoogle(request, context)
            viewModel.handleSignInResult(result)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        AccountScreen(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onSignInClick = {
                onSignInClick()
            },
            onSignOutClick = {
                viewModel.signOut()
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
suspend fun signInWithGoogle(
    request: GetCredentialRequest, context: Context
): SignInResult {
    val credentialManager = CredentialManager.create(context)

    delay(250)
    return try {
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )

        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
        Log.i(TAG, "Sign in Successful! token=${credential.idToken}")
        SignInResult.Success(
            idToken = credential.idToken,
            displayName = credential.displayName,
            email = credential.id
        )
    } catch (e: GetCredentialCancellationException) {
        Log.i(TAG, "Sign-in cancelled by user")
        SignInResult.Cancelled
    } catch (e: NoCredentialException) {
        Log.e(TAG, "No credentials found", e)
        SignInResult.Error(SignInErrorType.NO_ACCOUNT)
    } catch (e: GoogleIdTokenParsingException) {
        Log.e(TAG, "Issue with parsing GoogleIdToken", e)
        SignInResult.Error(SignInErrorType.PARSING_ERROR)
    } catch (e: GetCredentialCustomException) {
        Log.e(TAG, "Custom credential request issue", e)
        SignInResult.Error(SignInErrorType.AUTHENTICATION_FAILED)
    } catch (e: GetCredentialException) {
        val errorType = when {
            e.message?.contains(
                "network",
                ignoreCase = true
            ) == true -> SignInErrorType.NETWORK_ERROR

            e.message?.contains("timeout", ignoreCase = true) == true -> SignInErrorType.TIMEOUT

            else -> SignInErrorType.UNKNOWN
        }
        Log.e(TAG, "Sign-in failed: ${e.message}", e)
        SignInResult.Error(errorType)
    } catch (e: Exception) {
        Log.e(TAG, "Unexpected error during sign-in", e)
        SignInResult.Error(SignInErrorType.UNKNOWN)
    }
}

fun generateSecureRandomNonce(byteLength: Int = 32): String {
    val randomBytes = ByteArray(byteLength)
    SecureRandom.getInstanceStrong().nextBytes(randomBytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUiState,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    when (uiState) {
        is AccountUiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is AccountUiState.SignedOut -> {
            SignedOutView(
                modifier = modifier,
                onSignInClick = onSignInClick
            )
        }

        is AccountUiState.SignedIn -> {
            SignedInView(
                modifier = modifier,1
                userName = uiState.userName,
                userEmail = uiState.userEmail,
                topicsCompleted = uiState.topicsCompleted,
                topicsInProgress = uiState.topicsInProgress,
                exercisesCompleted = uiState.exercisesCompleted,
                onSignOutClick = onSignOutClick
            )
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SignedOutView(modifier: Modifier = Modifier, onSignInClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to DSLearn",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sign in to track your progress\nand unlock all features",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "What you'll get:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                BenefitItem(
                    icon = Icons.Outlined.CheckCircle,
                    title = "Track Your Progress",
                    description = "Save your completed topics and exercises",
                    iconBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                BenefitItem(
                    icon = Icons.Outlined.Settings,
                    title = "Sync Across Devices",
                    description = "Access your learning journey anywhere",
                    iconBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    iconTint = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                BenefitItem(
                    icon = Icons.Outlined.Lock,
                    title = "Unlock New Topics",
                    description = "Complete topics to unlock the next ones",
                    iconBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onSignInClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Sign in with Google", style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your data is secure and private",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SignedInView(
    modifier: Modifier = Modifier,
    userName: String,
    userEmail: String,
    topicsCompleted: Int,
    topicsInProgress: Int,
    exercisesCompleted: Int,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.first().uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .offset(y = (-24).dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Outlined.CheckCircle,
                value = topicsCompleted.toString(),
                label = "Topics Completed",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Outlined.Settings,
                value = topicsInProgress.toString(),
                label = "Topics In Progress",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Outlined.Create,
                value = exercisesCompleted.toString(),
                label = "Exercises Completed",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            MenuItemCard(
                icon = Icons.Outlined.Person, title = "Edit Profile", onClick = { })

            Spacer(modifier = Modifier.height(8.dp))

            MenuItemCard(
                icon = Icons.Outlined.Notifications, title = "Notifications", onClick = { })

            Spacer(modifier = Modifier.height(8.dp))

            MenuItemCard(
                icon = Icons.Outlined.Settings, title = "Preferences", onClick = { })

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Support",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            MenuItemCard(
                icon = Icons.Outlined.Info, title = "Help & Support", onClick = { })

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onSignOutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun BenefitItem(
    icon: ImageVector,
    title: String,
    description: String,
    iconBackgroundColor: Color,
    iconTint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = iconTint
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MenuItemCard(
    icon: ImageVector, title: String, onClick: () -> Unit
) {
    Card(
        onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

