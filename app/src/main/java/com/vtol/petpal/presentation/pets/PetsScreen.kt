package com.vtol.petpal.presentation.pets

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.common.components.DashedRoundedBorder
import com.vtol.petpal.presentation.common.components.SecondaryButton
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.pets.components.PetCard
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.SemiTransparentPurple
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.showToast

@Composable
fun PetsScreen(
    state: PetsState,
    navigateToAddPetScreen: () -> Unit,
    onScheduleClick: (String) -> Unit,
    onCardClick: (String) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column {
            // Top content
            Column(
                modifier = Modifier
                    .background(petPalGradient)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 32.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "My Pets",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    AppIconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        icon = R.drawable.ic_add,
                    ) {
                        if (state.pets.size < 2) {
                            navigateToAddPetScreen()

                        } else {
                            context.showToast("Upgrade to premium")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.error != null -> {
                LaunchedEffect(state.error) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                val pets = state.pets
                if (pets.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        DashedRoundedBorder(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .padding(vertical = 28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(containerColor = SemiTransparentPurple)
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .size(24.dp),
                                        painter = painterResource(R.drawable.ic_pets_outlined),
                                        contentDescription = "",
                                        tint = LightPurple
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No pets yet\nTap + to add one",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = LightPurple
                                )
                                Spacer(modifier = Modifier.height(22.dp))
                                SecondaryButton(buttonTxt = "Add Pet") { navigateToAddPetScreen() }
                            }
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.offset(y = (-28).dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    items(pets) { pet ->
                        PetCard(
                            pet = pet,
                            onScheduleClick = { onScheduleClick(it) },
                            onCardClick = { onCardClick(pet.id) },
                            task = state.firstTasks[pet.id]
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PetsPreview() {
    PetPalTheme {
        PetsScreen(
            state = PetsState(),
            onCardClick = {},
            onScheduleClick = {},
            navigateToAddPetScreen = {}
        )
    }
}