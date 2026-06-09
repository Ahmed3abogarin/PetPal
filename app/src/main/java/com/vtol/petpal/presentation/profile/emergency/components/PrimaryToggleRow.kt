package com.vtol.petpal.presentation.profile.emergency.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun PrimaryToggleRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (checked) MainPurple.copy(alpha = 0.06f) else Color(0xFFF9FAFB))
            .border(1.dp, if (checked) MainPurple.copy(alpha = 0.3f) else Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (checked) MainPurple.copy(alpha = 0.12f) else Color(0xFFE5E7EB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = if (checked) MainPurple else Color(0xFF9CA3AF),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = "Set as primary contact",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (checked) MainPurple else Color(0xFF374151)
            )
            Text(
                text = "Shown first in your emergency list",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor       = Color.White,
                checkedTrackColor       = MainPurple,
                uncheckedThumbColor     = Color.White,
                uncheckedTrackColor     = Color(0xFFD1D5DB),
                uncheckedBorderColor    = Color.Transparent,
                checkedBorderColor      = Color.Transparent,
            )
        )
    }
}