package com.vtol.petpal.presentation.profile.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.ValidationUtils

@Composable
fun ChangePhoneDialog(onDismiss: () -> Unit, onConfirm: (name: String) -> Unit) {

    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }


    AlertDialog(
        containerColor = BackgroundColor,
        onDismissRequest = { onDismiss() },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ExtraLightPurple)
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(14.dp)
                            .size(24.dp),
                        painter = painterResource(R.drawable.ic_phone),
                        contentDescription = null,
                        tint = MainPurple
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Change phone number", fontSize = 20.sp)
                    Text(
                        text = "Update the number associated with your account",
                        lineHeight = 16.sp,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        text = {
            PetTextField(
                value = phone,
                onValueChanged = {
                    phone = it
                    phoneError = ValidationUtils.validatePhone(phone)
                },
                placeHolder = "+1 234 567 8900",
                error = phoneError
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(phone) },
                enabled = phoneError == null
            ) { Text(text = "Update", color = MainPurple) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = "Cancel", color = Color.Black) }
        }
    )
}

@Preview
@Composable
fun MyPreviewPN(){
    PetPalTheme {
        ChangePhoneDialog(
            onDismiss= {}
        ) { }
    }
}