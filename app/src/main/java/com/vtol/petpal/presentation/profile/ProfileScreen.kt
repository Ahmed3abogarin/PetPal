package com.vtol.petpal.presentation.profile

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.ui.theme.BackgroundColor
import androidx.core.net.toUri
import com.vtol.petpal.presentation.profile.components.SettingsButton
import com.vtol.petpal.presentation.profile.components.ProfileInfoCard
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Red
import com.vtol.petpal.util.Resource
import com.vtol.petpal.util.showToast

@Composable
fun ProfileScreen(
    user: Resource<User>,
    state: ProfileUiState,
    petsCount: Int = 0,
    doneTasks: Int = 0,
    navigateToFeedBack: () -> Unit,
    event: (ProfileEvents) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column {
            // Top content
            val colors = listOf(Color(0XFF8638FE), Color(0XFFA266FF))
            // Gradient BG color
            val gradient = Brush.linearGradient(colors)



            Column(
                modifier = Modifier
                    .background(gradient)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 32.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "My Profile",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    FilledIconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 14.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(
                                alpha = 0.5f
                            )
                        ),
                        onClick = {
                            context.showToast()
                        }) {
                        Icon(
                            modifier = Modifier.padding(12.dp),
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }



                Spacer(modifier = Modifier.height(18.dp))


                // User Image + add icon button
                Box(
                    modifier = Modifier
                        .size(152.dp)
                        .clip(CircleShape)
                        .padding(16.dp),
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.img_profile_ph),
                        contentScale = ContentScale.Crop,
                        contentDescription = "profile image"
                    )

                    Image(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        painter = painterResource(R.drawable.ic_profile_add),
                        contentScale = ContentScale.Crop,
                        contentDescription = "profile image"
                    )
                }

                when (user) {
                    is Resource.Loading -> {
                        Text(
                            text = "Loading",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))


                        // User email
                        Text(
                            text = "Loading",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W300,
                            color = Color.White
                        )
                    }

                    is Resource.Success -> {
                        // Username
                        Text(
                            text = user.data.name,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))


                        // User email
                        Text(
                            text = user.data.email,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W300,
                            color = Color.White
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
                Spacer(modifier = Modifier.height(12.dp))


            }

            ProfileInfoCard(
                modifier = Modifier.offset(y = (-24).dp),
                petsCount = petsCount,
                vetVisits = state.vetVisits,
                doneTasks = doneTasks
            )


        }


        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "PREFERENCES",
            fontSize = 15.sp,
            color = MainPurple,
            fontWeight = FontWeight.W400
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {
            SettingsButton(buttonTxt = "Settings", icon = R.drawable.ic_settings) {}
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )

            SettingsButton(buttonTxt = "Notification", icon = R.drawable.ic_notification) {}
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )
            SettingsButton(buttonTxt = "Language", icon = R.drawable.ic_language) {}
        }






        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "SUPPORT",
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
                buttonTxt = "Emergency contacts",
                bgColor = Color(0XFFFFF4DE),
                icon = R.drawable.ic_phone
            ) {}
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )

            SettingsButton(
                buttonTxt = "Invite friends",
                bgColor = Color(0XFFE6F5E8),
                icon = R.drawable.ic_invite
            ) {}
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )
            SettingsButton(
                buttonTxt = "Send feedback",
                bgColor = Color(0XFFFFE3EC),
                icon = R.drawable.ic_chat
            ) { navigateToFeedBack() }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )
            SettingsButton(buttonTxt = "Terms & Privacy", icon = R.drawable.ic_terms) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://ahmed3abogarin.github.io/PetPal-privacy-policy".toUri()
                )
                context.startActivity(intent)
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )


        }

        // Sign out Button
        Spacer(modifier = Modifier.height(18.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, Red.copy(alpha = 0.3f))
        ) {
            SettingsButton(
                buttonTxt = "Sign out",
                txtColor = Red,
                bgColor = Color(0XFFFFEAEE),
                icon = R.drawable.ic_signout
            ) {
                event(ProfileEvents.SignOut)
            }
        }


        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Preview(device = "id:pixel_7_pro")
@Composable
fun SettingsButtonPreview() {
    PetPalTheme {
        ProfileScreen(
            state = ProfileUiState(),
            event = { },
            user = Resource.Success(
                User(
                    name = "John Doe",
                    email = "johnson.mckinley@examplepetstore.com"
                )
            ),
            navigateToFeedBack = {}
        )
    }
}