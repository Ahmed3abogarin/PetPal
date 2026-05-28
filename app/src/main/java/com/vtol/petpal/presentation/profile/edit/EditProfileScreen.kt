package com.vtol.petpal.presentation.profile.edit

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.presentation.common.UserUiState
import com.vtol.petpal.presentation.common.components.LoadingIndicator
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.presentation.profile.edit.components.ChangeNameDialog
import com.vtol.petpal.presentation.profile.edit.components.ChangePasswordDialog
import com.vtol.petpal.presentation.profile.edit.components.ChangePhoneDialog
import com.vtol.petpal.presentation.tasks.SectionLabel
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Red
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.showToast
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun EditProfileScreen(
    state: UserUiState,
) {
    val context = LocalContext.current

    var dialog by remember { mutableStateOf<EditProfileDialog>(EditProfileDialog.None) }

    // TODO: Check the color of the texts could be used as place holder to match the color and to simplify the complexity

    if (state.isLoading) {
        LoadingIndicator()
        return
    }

    // Crop launcher — receives the cropped URI result
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let {
                // event(ProfileEvents.UpdateImage(it))
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

    LaunchedEffect(state.error) {
        if (state.error != null) {
            context.showToast(state.error)
        }
    }

    state.user?.let {
        val isImgEmpty = it.imgPath.isEmpty()
        Column(
            modifier = Modifier
                .background(BackgroundColor)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top content
            Column(
                modifier = Modifier
                    .background(petPalGradient)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 32.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppIconButton { }
                    Text(
                        text = "My Profile",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    // TODO: Could be removed

                    AppIconButton(icon = R.drawable.ic_done) { }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 4.dp)
                        .size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(
                                width = if (isImgEmpty) 0.dp else 2.dp,
                                color = LightPurple,
                                shape = CircleShape
                            )
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
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp),
                            colors = CardDefaults.cardColors(containerColor = MainPurple),
                            shape = CircleShape,
                            border = BorderStroke(0.3.dp, Color.White)

                        ) {
                            Icon(
                                modifier = Modifier
                                    .clickable { imagePickerLauncher.launch("image/*") }
                                    .padding(8.dp)
                                    .size(12.dp),
                                painter = painterResource(R.drawable.ic_edit_v2),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }


                Card(
                    onClick = {},
                    border = BorderStroke(width = 0.3.dp, color = Color.Red.copy(alpha = 0.4f)),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        text = "Remove",
                        fontSize = 12.sp,
                        color = Red.copy(alpha = 0.7f),
                    )
                }
            }

            // Main layout
            Column(
                modifier = Modifier
                    .offset(y = (-22).dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = "Personal Info",
                    fontSize = 15.sp,
                    color = MainPurple,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.height(18.dp))

                SectionLabel("Username")
                //TODO: Change the value (text) color in this specific screen or in general.
                PetTextField(
                    leadingIcon = R.drawable.ic_person,
                    iconSize = 22.dp,
                    fontSize = 14.sp,
                    readOnly = true,
                    trailingIcon = R.drawable.ic_edit,
                    placeHolder = "John Doe",
                    value = it.name,
                    onValueChanged = {},
                    onTrailingClicked = { dialog = EditProfileDialog.Username }
                )
                Spacer(modifier = Modifier.height(16.dp))

                SectionLabel("Email")
                PetTextField(
                    leadingIcon = R.drawable.ic_mail,
                    iconSize = 22.dp,
                    readOnly = true,
                    fontSize = 14.sp,
                    placeHolder = it.email,
                    value = "",
                    onValueChanged = {},
                )

                Spacer(modifier = Modifier.height(16.dp))

                SectionLabel("Phone")
                PetTextField(
                    leadingIcon = R.drawable.ic_phone,
                    iconSize = 22.dp,
                    fontSize = 14.sp,
                    readOnly = true,
                    trailingIcon = R.drawable.ic_edit,
                    placeHolder = "+1 234 567 8900",
                    value = it.phoneNumber,
                    onValueChanged = {},
                    onTrailingClicked = { dialog = EditProfileDialog.Phone }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SectionLabel("Passowrd")
                PetTextField(
                    leadingIcon = R.drawable.ic_lock,
                    iconSize = 22.dp,
                    fontSize = 14.sp,
                    readOnly = true,
                    trailingIcon = R.drawable.ic_edit,
                    placeHolder = "", // TODO: Should match the others
                    value = "Password",  // ••••••••
                    onValueChanged = { },
                    onTrailingClicked = { dialog = EditProfileDialog.Password }
                )
            }


            // TODO: Show the deletion warning dialog before proceeding with the deletion request
            Card(
                onClick = { dialog = EditProfileDialog.Delete },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
                border = BorderStroke(0.3.dp, Color.Red.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0XFFFFEAEE))
                                .padding(14.dp)
                                .size(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.Default.RestoreFromTrash,
                                contentDescription = null,
                                tint = Red
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Delete account",
                                fontSize = 16.sp,
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Permanently removes all data",
                                fontSize = 14.sp,
                                color = Color.Red.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        tint = Red,
                        contentDescription = ""
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }


    when (dialog) {
        EditProfileDialog.Username -> {
            // TODO
            ChangeNameDialog(onDismiss = {}, onConfirm = {})
        }

        EditProfileDialog.Password -> {
            ChangePasswordDialog(
                onDismiss = {},
                onConfirm = { _, _  ->
                    // TODO

                    dialog = EditProfileDialog.None
                }
            )
        }

        EditProfileDialog.Phone -> {
            // TODO
            ChangePhoneDialog(onDismiss = {}, onConfirm = {})

        }

        EditProfileDialog.Delete -> {
            // TODO

        }

        EditProfileDialog.None -> Unit
    }


}

@Preview
@Composable
fun EditPreview() {
    PetPalTheme {
        EditProfileScreen(
            UserUiState(
                user = User(
                    name = "Ahmed Adil",
                    email = "ahmedadilabogarin@gmail.com",
                    phoneNumber = "0560634785"
                )
            ),
        )
    }
}