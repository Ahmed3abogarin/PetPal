package com.vtol.petpal.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        // Prevent clicks while loading
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = Color(0xFFD0BCFF),
            contentColor = Color.White
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // 1. Size the progress indicator appropriately for a button
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }

            // 2. Hide BOTH text and icon by controlling the alpha of the Row
            Row(
                modifier = Modifier.padding(vertical = 6.dp).alpha(if (isLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        modifier = Modifier.padding(end = 8.dp),
                        imageVector = it,
                        contentDescription = null
                    )
                }

                Text(
                    text = text,
                    fontSize = 20.sp,
                )
            }
        }
    }
}