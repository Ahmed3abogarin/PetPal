package com.vtol.petpal.presentation.home.components

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.material.color.MaterialColors.ALPHA_DISABLED
import com.google.android.material.color.MaterialColors.ALPHA_FULL
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.presentation.components.TextFieldVariant
import com.vtol.petpal.presentation.components.filledTextFieldColors
import com.vtol.petpal.ui.theme.PetPalTheme


@Composable
fun <T> MyDropDownMenu(
    modifier: Modifier = Modifier,
    expendedIcon: ImageVector = Icons.Default.KeyboardArrowUp,
    closedIcon: ImageVector = Icons.Default.KeyboardArrowDown,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    variant: TextFieldVariant = TextFieldVariant.OUTLINED,
    items: List<T>,
    error: String? = null,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        when(item) {
            is PetGender -> {
                LargeDropdownMenuItem(
                    text = item.genderTxt,
                    selected = selected,
                    enabled = itemEnabled,
                    onClick = onClick,
                    icon = item.icon
                )
            }
            is TaskType -> {
                LargeDropdownMenuItem(
                    text = item.txt,
                    selected = selected,
                    enabled = itemEnabled,
                    onClick = onClick,
                    icon = item.icon
                )
            }
            else -> LargeDropdownMenuItem(
                text = item.toString(),
                selected = selected,
                enabled = itemEnabled,
                onClick = onClick,
            )
        }
    }
) {
    val focusManger = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }
    val selectedItem  = items.getOrNull(selectedIndex)

    Box(modifier = modifier.height(IntrinsicSize.Min)) {

        when (variant) {
            TextFieldVariant.OUTLINED -> {
                OutlinedTextField(
                    label = { Text(label) },
                    value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
                    enabled = enabled,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    isError = error != null,
                    supportingText = {
                        if (error != null){
                            Text(error)
                        }
                    },
                    trailingIcon = {
                        val icon = if (expanded) expendedIcon else closedIcon
                        Icon(icon, "")
                    },
                    leadingIcon = if (selectedItem is PetGender) {
                        {
                            Image(
                                painter = painterResource(selectedItem.icon),
                                contentDescription = null
                            )
                        }
                    } else null,
                    onValueChange = { },
                    readOnly = true,
                )

            }
            TextFieldVariant.Filled -> {

                TextField (
                    label = { Text(label) },
                    value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
                    enabled = enabled,
                    colors = filledTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    isError = error != null,
                    supportingText = {
                        if (error != null){
                            Text(error)
                        }
                    },
                    trailingIcon = {
                        val icon = if (expanded) expendedIcon else closedIcon
                        Icon(icon, "")
                    },
                    leadingIcon = if (selectedItem is PetGender) {
                        {
                            Image(
                                painter = painterResource(selectedItem.icon),
                                contentDescription = null
                            )
                        }
                    } else null,
                    onValueChange = { },
                    readOnly = true,
                )

            }

        }



        // Transparent clickable surface on top of OutlinedTextField
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            PetPalTheme {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                ) {
                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                        if (notSetLabel != null) {
                            item {
                                LargeDropdownMenuItem(
                                    text = notSetLabel,
                                    selected = false,
                                    enabled = false,
                                    onClick = { },
                                )
                            }
                        }
                        itemsIndexed(items) { index, item ->
                            val selectedItem = index == selectedIndex
                            drawItem(
                                item,
                                selectedItem,
                                true
                            ) {
                                Log.v("TTTTT","index: $index, item: $item")
                                onItemSelected(index, item)

                                // to loss the focus
                                focusManger.clearFocus()
                                expanded = false
                            }

//                            if (index < items.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
//                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = ALPHA_DISABLED)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = ALPHA_FULL)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = ALPHA_FULL)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            icon?.let {
                Image(modifier = Modifier.size(32.dp), painter = painterResource(icon), contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
