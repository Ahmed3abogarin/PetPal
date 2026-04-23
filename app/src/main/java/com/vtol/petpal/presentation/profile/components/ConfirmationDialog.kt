package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.Red


@Composable
fun ConfirmationDialog(
    visible: Boolean,
    onDismiss: () -> Unit = {},
    onSignOutClicked: () -> Unit = {}
) {

    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onDismiss() }
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                        .align(Alignment.Center)
                        .clickable(false){},
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Red.copy(alpha = 0.14f)),
                        shape = CircleShape,
                        border = BorderStroke(6.dp, Red.copy(alpha = 0.2f))
                    ) {

                        Icon(
                            modifier = Modifier
                                .padding(30.dp)
                                .size(46.dp),
                            painter = painterResource(id = R.drawable.ic_signout),
                            contentDescription = "",
                            tint = Red
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))



                    Text(
                        text = "Sign out?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(18.dp))


                    Text(
                        text = "You'll be logged out of your PetPal account. Your pets will be safe.",
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MainPurple.copy(alpha = 0.2f))
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                            Image(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                contentDescription = ""
                            )
                            Image(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                painter = painterResource(id = R.drawable.location_img),
                                contentScale = ContentScale.Crop,
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = "Your 2 pets data will stay saved.",
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = MainPurple
                        )

                    }

                    Spacer(modifier = Modifier.height(42.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSignOutClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = Red),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_signout),
                                contentDescription = ""
                            )
                            Text(
                                text = "Yes, sign out",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )

                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismiss,
                        border = BorderStroke(width = 1.dp, color = MainPurple.copy(alpha = 0.2f)),
                        colors = ButtonDefaults.buttonColors(containerColor = MainPurple.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 6.dp),
                            text = "Cancel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MainPurple
                        )
                    }
                }


            }

        }
    }
}


@Preview
@Composable
fun ConfirmationDialogPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        ConfirmationDialog(true)
    }

}