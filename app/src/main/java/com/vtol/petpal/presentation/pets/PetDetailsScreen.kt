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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
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
import com.vtol.petpal.presentation.components.BackArrow
import com.vtol.petpal.presentation.pets.tabs.GalleryTab
import com.vtol.petpal.presentation.pets.tabs.HealthTab
import com.vtol.petpal.presentation.pets.tabs.OverviewTab
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.Resource
import com.vtol.petpal.util.toAgeString

@Composable
fun PetDetailsScreen(modifier: Modifier = Modifier, petViewModel: PetDetailsViewModel) {
    val state = petViewModel.petState.collectAsState().value

    when (state) {
        is Resource.Success -> {
            state.data?.let {
                PetDetailsScreenContent(pet = it)
            }
        }

        is Resource.Error -> {
                Text(state.message)
        }

        is Resource.Loading -> {
            CircularProgressIndicator()

        }
    }
}

@Composable
private fun PetDetailsScreenContent(modifier: Modifier = Modifier, pet: Pet) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackArrow { }

            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = ""
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Add suitable placeholder image
        Box {
            Box(
                modifier = Modifier
                    .size(152.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, Color.Gray, CircleShape)
            )
            {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    model = ImageRequest.Builder(context).data("").build(),
                    contentDescription = "pet profile image",
                    placeholder = painterResource(R.drawable.ic_unknown)
                )

            }

            // TODO: displays only if the image is null
            if (true) {
                FilledIconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp),
                    shape = CircleShape,
                    onClick = {}) {
                    Icon(Icons.Default.Edit, contentDescription = "")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = pet.petName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = pet.breed ?: "Unknown"
        )
        Text(
            text = pet.birthDate.toAgeString()
        )
        // TODO: Figure out a way to add the birthday
//            Text(
//                text = "Since 12th oct 2025"
//            )


        // ------------------------- Tabs ---------------------------------
        Spacer(modifier= Modifier.height(20.dp))
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(1) }
        val tabs = listOf("Overview", "Health", "Gallery")

        // A column will act as a container for the tabs
        PrimaryTabRow (modifier = Modifier.fillMaxWidth(),containerColor = Color.White, selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )

            }
        }

        when(selectedTabIndex){
            0 -> OverviewTab()
            1 -> HealthTab()
            2 -> GalleryTab(isPremium = false){}

        }



    }


}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {


    }
}