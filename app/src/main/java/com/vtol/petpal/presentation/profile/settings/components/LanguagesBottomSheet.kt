package com.vtol.petpal.presentation.profile.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = null,
        tonalElevation = 0.dp,
    ) {
        LanguageSheetContent(onDismiss = onDismiss)
    }
}

@Composable
fun LanguageSheetContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        // Handle
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
                .width(36.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0DAF5))
        )

        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Language",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A2E)
            )
            Text(
                text = "Choose your preferred language",
                fontSize = 12.sp,
                color = Color(0xFF9990C0),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        HorizontalDivider(color = Color(0xFFF0EEFA), thickness = 0.5.dp)

        // English row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F1FF))
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE8E2FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = null,
                    tint = Color(0xFF6C47FF),
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "English",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A2E)
                )
                Text(
                    text = "Currently selected",
                    fontSize = 12.sp,
                    color = Color(0xFF9990C0),
                    modifier = Modifier.padding(top = 1.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6C47FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        // Coming soon hint
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF5F4FB))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = Color(0xFFB8B0D8),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "More languages are coming soon. Stay tuned for updates.",
                fontSize = 12.sp,
                color = Color(0xFF9990C0),
                lineHeight = 16.sp
            )
        }

        // Done button
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 14.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C47FF))
        ) {
            Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}