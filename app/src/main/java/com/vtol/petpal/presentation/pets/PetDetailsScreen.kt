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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.presentation.components.BackArrow
import com.vtol.petpal.presentation.pets.tabs.GalleryTab
import com.vtol.petpal.presentation.pets.tabs.HealthTab
import com.vtol.petpal.presentation.pets.tabs.OverviewTab
import com.vtol.petpal.util.showToast
import com.vtol.petpal.util.toAgeString

@Composable
fun PetDetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailsState,
    navigateUp: () -> Unit,
    onCheckedChanged: (id: Int, isCompleted: Boolean) -> Unit,
    onAddWeightClicked: (WeightRecord) -> Unit,
) {

    val context = LocalContext.current

    LaunchedEffect(state.error) {
        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        navigateUp()
    }

    Scaffold(
        containerColor = Color(0XFFF8F4FF),
        floatingActionButton = {
            FloatingActionButton(onClick = { context.showToast() }, shape = CircleShape) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { _ ->

        if (state.pet != null) {

            val pet = state.pet


            Column(
                modifier = modifier
                    .fillMaxSize()
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

                Box(
                    modifier = Modifier
                        .size(152.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .border(2.dp, Color.Gray, CircleShape)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = ImageRequest.Builder(context).data(pet.imagePath).build(),
                        error = painterResource(R.drawable.pet_placeholder),
                        contentDescription = "pet profile image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.pet_placeholder)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = pet.petName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = pet.birthDate.toAgeString())


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
                    0 -> OverviewTab(
                        state = state,
                        onCheckedChanged = { id, isCompleted -> onCheckedChanged(id, isCompleted) }
                    )

                    1 -> HealthTab(
                        onAddWeightClicked = { onAddWeightClicked(it) },
                        weightList = state.lastWeight,
                        state = state
                    )

                    2 -> GalleryTab(isPremium = false) { context.showToast() }
                }
            }
        }

        if (state.isLoading){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}