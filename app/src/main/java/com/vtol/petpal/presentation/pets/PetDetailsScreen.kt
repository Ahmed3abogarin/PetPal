package com.vtol.petpal.presentation.pets

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.vtol.petpal.domain.model.weight.WeightRange
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.pets.tabs.GalleryTab
import com.vtol.petpal.presentation.pets.tabs.HealthTab
import com.vtol.petpal.presentation.pets.tabs.OverviewTab
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.showToast
import com.vtol.petpal.util.toAgeString

@Composable
fun PetDetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailsState,
    navigateUp: () -> Unit,
    onRangeChanged: (WeightRange) -> Unit,
    onAddWeightClicked: (WeightRecord) -> Unit,
    onAddTaskClick: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(state.error) {
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            navigateUp()
        }
    }


    if (state.pet != null) {

        val pet = state.pet


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            Column(
                modifier = modifier
                    .background(petPalGradient)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .padding(bottom = 28.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppIconButton {
                        navigateUp()
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AppIconButton(icon = R.drawable.ic_pet_card) { context.showToast() }
                        AppIconButton(icon = R.drawable.ic_edit) { context.showToast() }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(122.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .border(3.dp, Color.White, CircleShape),
                        model = ImageRequest.Builder(context).data(pet.imagePath).build(),
                        error = painterResource(R.drawable.pet_placeholder),
                        contentDescription = "pet profile image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.pet_placeholder)
                    )

                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(if (pet.isActive) Color.Green else Color.Gray)
                            .border(width = 2.dp, color = Color.White, shape = CircleShape)
                            .size(24.dp)
                            .align(Alignment.BottomEnd),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = pet.petName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = pet.birthDate.toAgeString(),
                        fontSize = 14.sp,
                        color = CellsBgPurple
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(5.dp)
                            .background(ExtraLightPurple)
                    )
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(ExtraLightPurple)
                            .padding(horizontal = 8.dp, vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${pet.specie} • ${pet.gender.name}",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }

                }
            }


            // ------------------------- Tabs ---------------------------------
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            val tabs = listOf("Overview", "Health", "Gallery")

            // A column will act as a container for the tabs

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-22).dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
            ) {

                PrimaryTabRow(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    selectedTabIndex = selectedTabIndex,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            width = 90.dp,
                            modifier = Modifier.tabIndicatorOffset(
                                matchContentSize = true,
                                selectedTabIndex = selectedTabIndex
                            ),
                            color = MainPurple
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    title,
                                    color = if (isSelected) MainPurple else LightPurple
                                )
                            }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> OverviewTab(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        state = state,
                        onAddTaskClick = onAddTaskClick,
                    )

                    1 -> HealthTab(
                        onAddWeightClicked = { record -> onAddWeightClicked(record) },
                        weightList = state.lastWeight,
                        onRangedChanged = {
                            onRangeChanged(it)
                        },
                        state = state
                    )

                    2 -> GalleryTab(isPremium = false) { context.showToast() }
                }
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


@Preview
@Composable
fun PetScreenPreview() {
    PetPalTheme {

        PetDetailsScreen(
            state = DetailsState(),
            onAddWeightClicked = {},
            navigateUp = {},
            onAddTaskClick = {},
            onRangeChanged = {}
        )
    }
}