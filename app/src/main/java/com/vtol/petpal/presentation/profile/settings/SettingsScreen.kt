package com.vtol.petpal.presentation.profile.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.profile.components.SettingsButton
import com.vtol.petpal.presentation.profile.settings.components.LanguageBottomSheet
import com.vtol.petpal.presentation.profile.settings.components.NotificationsBottomSheet
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.getVersionName
import com.vtol.petpal.util.showToast

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsUiState,
    onToggleNotification: (Boolean) -> Unit,
    navigateToRestorePurchases: () -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    var showNotificationSheet by remember { mutableStateOf(false) }
    var showLanguagesSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp)
                .padding(horizontal = 6.dp)
        ) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                onClick = navigateUp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "back arrow"
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Settings",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }


        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "PREMIUM",
            fontSize = 15.sp,
            color = MainPurple,
            fontWeight = FontWeight.W400
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {
            SettingsButton(
                buttonTxt = "Upgrade to Premium",
                description = "Unlock exclusive features",
                bgColor = Color(0XFFFFF4DE),
                icon = R.drawable.ic_crown
            ) { context.showToast() }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )

            SettingsButton(
                buttonTxt = "Restore Purchase",
                description = "Restore your previous purchase",
                icon = R.drawable.ic_restore
            ) { navigateToRestorePurchases() }

        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "GENERAL",
            fontSize = 15.sp,
            color = MainPurple,
            fontWeight = FontWeight.W400
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {
            SettingsButton(
                buttonTxt = "Notification",
                description = "Manage notification settings",
                icon = R.drawable.ic_notification
            ) { showNotificationSheet = true }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )

            SettingsButton(
                buttonTxt = "Language",
                description = "Choose your preferred language",
                icon = R.drawable.ic_language
            ) { showLanguagesSheet = true }

        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "ABOUT",
            fontSize = 15.sp,
            color = MainPurple,
            fontWeight = FontWeight.W400
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {
            SettingsButton(
                buttonTxt = "App Version",
                trailingText = context.getVersionName(),
                clickable = false,
                icon = R.drawable.ic_info
            ) {}
        }
    }

    if (showNotificationSheet) {
        NotificationsBottomSheet(
            isEnabled = state.isNotificationEnabled,
            onDismiss = { showNotificationSheet = false },
            onToggle = { onToggleNotification(it) }
        )
    }

    if (showLanguagesSheet) {
        LanguageBottomSheet { showLanguagesSheet = false }
    }
}


@Preview
@Composable
fun SettingsPreview() {
    PetPalTheme {
        SettingsScreen(state = SettingsUiState(), onToggleNotification = {}, navigateToRestorePurchases = {}) {}
    }
}