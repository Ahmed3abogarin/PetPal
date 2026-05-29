package com.vtol.petpal.presentation.profile

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.presentation.common.components.LoadingIndicator
import com.vtol.petpal.presentation.profile.components.ConfirmationDialog
import com.vtol.petpal.presentation.profile.components.SettingsButton
import com.vtol.petpal.presentation.profile.components.ProfileInfoCard
import com.vtol.petpal.presentation.profile.components.ProfileShimmerEffect
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Red
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.ShareManager.openWebsite
import com.vtol.petpal.util.ShareManager.shareApp
import com.vtol.petpal.util.getVersionName
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    petsCount: Int = 0,
    doneTasks: Int = 0,
    navigateToFeedBack: () -> Unit,
    navigateToEdit: () -> Unit,
    event: (ProfileEvents) -> Unit
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }


    // Crop launcher — receives the cropped URI result
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let {
                event(ProfileEvents.UpdateImage(it))
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val destUri = Uri.fromFile(
                File(context.cacheDir, "cropped_pet_${System.currentTimeMillis()}.jpg")
            )
            val cropIntent = UCrop.of(it, destUri)
                .withAspectRatio(1f, 1f)
                .withOptions(UCrop.Options().apply {
                    setCircleDimmedLayer(true)       // circular crop overlay
                    setShowCropGrid(false)
                    setShowCropFrame(false)
                    setToolbarTitle("Crop Profile Photo")
                    setCompressionQuality(100)
                })
                .withMaxResultSize(512, 512)
                .getIntent(context)

            cropLauncher.launch(cropIntent)
        }
    }

    if (state.isUserLoading) {
        ProfileShimmerEffect()
        return
    }

    state.error?.let {
        LaunchedEffect(it) {
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column {
            // Top content
            Column(
                modifier = Modifier
                    .background(petPalGradient)
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
                        onClick = navigateToEdit
                    ) {
                        Icon(
                            modifier = Modifier.padding(12.dp),
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                state.user?.let {
                    // User Image + add icon button
                    val isImgEmpty = it.imgPath.isEmpty()
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(width = if (isImgEmpty) 0.dp else 1.dp, color= Color.DarkGray, shape = CircleShape)
                                .background(LightPurple),
                            model = ImageRequest.Builder(context)
                                .data(it.imgPath)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.img_profile_ph),
                            error = painterResource(R.drawable.img_profile_ph),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Profile image"
                        )

                        // Upload overlay
                        if (state.isImageUploading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(
                                        Color.Black.copy(alpha = 0.45f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        }

                        // Add image button
                        if (isImgEmpty && !state.isImageUploading) {
                            Image(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .clickable {
                                        imagePickerLauncher.launch("image/*")
                                    },
                                painter = painterResource(R.drawable.ic_profile_add),
                                contentDescription = "Change image"
                            )
                        }
                    }


                    // Username
                    Text(
                        text = it.name,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // User email
                    Text(
                        text = it.email,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W300,
                        color = Color.White
                    )
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
            ) { shareApp(context) }
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
                openWebsite(
                    context,
                    "https://ahmed3abogarin.github.io/PetPal-privacy-policy".toUri()
                )
            }
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
                showDialog = true
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        // version name
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = Color.Gray.copy(0.2f),
                thickness = 0.5.dp
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "Version name: ${context.getVersionName()}",
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = Color.Gray.copy(0.2f),
                thickness = 0.5.dp
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))



    ConfirmationDialog(
        visible = showDialog,
        onDismiss = { showDialog = false },
        onSignOutClicked = {
            showDialog = false
            event(ProfileEvents.SignOut)
        }
    )
}


@Preview(device = "id:pixel_7_pro")
@Composable
fun SettingsButtonPreview() {
    PetPalTheme {
        ProfileScreen(
            state = ProfileUiState(
                user = User(
                    imgPath = "",
                    name = "John Doe",
                    email = "john.c.calhoun@examplepetstore.com"
                )
            ),
            event = { },
            navigateToFeedBack = {},
            navigateToEdit = {}
        )
    }
}