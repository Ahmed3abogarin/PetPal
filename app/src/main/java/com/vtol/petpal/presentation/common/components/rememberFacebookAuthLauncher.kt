package com.vtol.petpal.presentation.common.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun rememberFacebookAuthLauncher(
    onSuccess: (token: String) -> Unit,
    onError: (FacebookException) -> Unit = {}
): () -> Unit {
    val callbackManager = remember { CallbackManager.Factory.create() }

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    onSuccess(result.accessToken.token)
                }
                override fun onCancel() {}
                override fun onError(error: FacebookException) {
                    onError(error)
                }
            }
        )
        onDispose { LoginManager.getInstance().unregisterCallback(callbackManager) }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    ) { _ -> }

    return {
        launcher.launch(listOf("email", "public_profile"))
    }
}