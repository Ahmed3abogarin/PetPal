package com.vtol.petpal.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.profile.components.SettingsButton
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "My Profile", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape),
            painter = painterResource(R.drawable.profile_img),
            contentScale = ContentScale.Crop,
            contentDescription = "profile image"
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Ahmed Adil", fontSize = 20.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        SettingsButton(
            leadIcon = R.drawable.ic_settings,
            text = "Settings"
        )
        SettingsButton(
            leadIcon = R.drawable.ic_bell,
            text = "Notification"
        )

        SettingsButton(
            leadIcon = R.drawable.ic_world,
            text = "Language"
        )

        SettingsButton(
            leadIcon = R.drawable.ic_contact,
            text = "Emergency contacts"
        )

        SettingsButton(
            leadIcon = R.drawable.ic_invite,
            text = "Invite friends"
        )

        SettingsButton(
            leadIcon = R.drawable.ic_help,
            text = "Invite friends"
        )

        SettingsButton(
            text = "Terms & Privacy"
        )







    }
}


@Preview
@Composable
fun ProfilePreview() {
    PetPalTheme {
        ProfileScreen()
    }
}