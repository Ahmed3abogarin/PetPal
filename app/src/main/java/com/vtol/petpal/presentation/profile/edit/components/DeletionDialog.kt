package com.vtol.petpal.presentation.profile.edit.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Red

@Composable
fun DeletionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    val list = listOf(
        "This action is permanent and cannot be undone.",
        "You'll lose access to all your data.",
        "This cannot be recovered once confirmed."
    )

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
                    .clickable(false) {},
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
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = "",
                        tint = Red
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Delete account?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(18.dp))


                Text(
                    text = "This action is permanent and cannot be undone.",
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))

                list.forEach { text ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.padding(top = 6.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.5.dp, color = Color.Red)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(12.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                        Text(
                            text = text,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Red),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = "Yes, delete my account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MainPurple
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DeletionPreview() {
    PetPalTheme {
        DeletionDialog(onDismiss = {}, onConfirm = {})
    }
}