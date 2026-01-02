package com.vtol.petpal.presentation.pets

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.components.BackArrow
import com.vtol.petpal.presentation.pets.tabs.GalleryTab
import com.vtol.petpal.presentation.pets.tabs.HealthTab
import com.vtol.petpal.presentation.pets.tabs.OverviewTab
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.showToast
import com.vtol.petpal.util.toAgeString

@Composable
fun PetDetailsScreen(
    modifier: Modifier = Modifier,
    petViewModel: PetDetailsViewModel,
    navigateUp: () -> Unit,
) {
    val state = petViewModel.state.collectAsState().value


    state.pet?.let {
        PetDetailsScreenContent(
            modifier = modifier,
            pet = it,
            task = state,
            navigateUp = navigateUp,
            onAddWeight = { petId, weight ->
                petViewModel.addWeight(petId,weight)
            }
        )

    }

    if (state.isLoading) CircularProgressIndicator()
    if (state.error != null) Text(text = state.error)
}

@Composable
private fun PetDetailsScreenContent(
    modifier: Modifier = Modifier,
    pet: Pet,
    task: DetailsState,
    navigateUp: () -> Unit,
    onAddWeight: (petId: String,weight:WeightRecord) -> Unit,
) {
    Scaffold(
        containerColor = Color(0XFFF8F4FF),
        floatingActionButton = {
            FloatingActionButton(onClick = {}, shape = CircleShape) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { paddingValues ->

        val context = LocalContext.current

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BackArrow {
                    navigateUp()
                }

                IconButton(onClick = {
                    context.showToast()
                }) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.Filled.Share,
                        contentDescription = ""
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // TODO: Add suitable placeholder image

            Box(
                modifier = Modifier
                    .size(152.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, Color.Gray, CircleShape)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    model = ImageRequest.Builder(context).data("").build(),
                    error = painterResource(R.drawable.ic_unknown),
                    contentDescription = "pet profile image",
                    placeholder = painterResource(R.drawable.ic_unknown)
                )

            }

//


            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = pet.petName,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = pet.birthDate.toAgeString())

            // TODO: Figure out a way to add the birthday
//            Text(
//                text = "Since 12th oct 2025"
//            )


            // ------------------------- Tabs ---------------------------------
            Spacer(modifier = Modifier.height(20.dp))
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            val tabs = listOf("Overview", "Health", "Gallery")

            // A column will act as a container for the tabs
            PrimaryTabRow(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> OverviewTab(state = task)
                1 -> HealthTab(
                    onAddWeightClicked = {
                        onAddWeight(pet.id, it)
                    },
                    weightList = listOf(
                        WeightRecord(weight = 2.5, unit = WeightUnit.KG, timestamp = 1000000000000),
                        WeightRecord(weight = 3.0, unit = WeightUnit.KG, timestamp = 1700003600000),
                        WeightRecord(weight = 2.8, unit = WeightUnit.KG, timestamp = 1700007200000),
                        WeightRecord(weight = 3.2, unit = WeightUnit.KG, timestamp = 1700010800000),
                        WeightRecord(weight = 3.5, unit = WeightUnit.KG, timestamp = 1700014400000),
                        WeightRecord(weight = 3.1, unit = WeightUnit.KG, timestamp = 1700018000000),
                        WeightRecord(weight = 3.4, unit = WeightUnit.KG, timestamp = 1700021600000)
                    )
                )

                2 -> GalleryTab(isPremium = false) {}
            }
        }
    }
}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        PetDetailsScreenContent(
            pet = Pet(
                petName = "Lionel Messi",
                breed = "Shirazi",
                birthDate = 123456789,
                gender = PetGender.Male
            ),
            task = DetailsState(),
            navigateUp = {},
            onAddWeight = { _, _ -> }
        )
    }
}