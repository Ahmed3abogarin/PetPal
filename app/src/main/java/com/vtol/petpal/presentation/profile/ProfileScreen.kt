package com.vtol.petpal.presentation.profile

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.presentation.common.UserViewModel
import com.vtol.petpal.presentation.profile.components.SettingsButton
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.util.Resource
import androidx.core.net.toUri

@Composable
fun ProfileScreen(viewmodel: UserViewModel, navigateToFeedback: () -> Unit) {
    val state by viewmodel.state.collectAsState()

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(top = 6.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "My Profile", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        // Todo : Add Async image
        Image(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape),
            painter = painterResource(R.drawable.profile_ph),
            contentScale = ContentScale.Crop,
            contentDescription = "profile image"
        )
//        Spacer(modifier = Modifier.height(12.dp))
        when (state) {
            is Resource.Loading -> {
                Text(
                    text = "Loading...",
                    fontSize = 28.sp,
                    color = Color.Gray
                )
            }

            is Resource.Success -> {
                Text(
                    text = (state as Resource.Success<User>).data.email,
                    fontSize = 20.sp,
                    color = Color.Gray
                )

            }

            is Resource.Error -> {
                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = "Guest",
                    fontSize = 28.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        SettingsButton(
            leadIcon = R.drawable.ic_settings,
            text = "Settings",
            onClick = {}
        )
        SettingsButton(
            leadIcon = R.drawable.ic_bell,
            text = "Notification",
            onClick = {}
        )

        SettingsButton(
            leadIcon = R.drawable.ic_world,
            text = "Language",
            onClick = {}
        )

        SettingsButton(
            leadIcon = R.drawable.ic_contact,
            text = "Emergency contacts",
            onClick = {}
        )

        SettingsButton(
            leadIcon = R.drawable.ic_invite,
            text = "Invite friends",
            onClick = {}
        )

        SettingsButton(
            leadIcon = R.drawable.ic_help,
            text = "Send Feedback",
            onClick = {
                navigateToFeedback()
            }
        )

        SettingsButton(
            text = "Terms & Privacy",
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW,
                    "https://ahmed3abogarin.github.io/PetPal-privacy-policy".toUri())
                context.startActivity(intent)
            }
        )


    }
}