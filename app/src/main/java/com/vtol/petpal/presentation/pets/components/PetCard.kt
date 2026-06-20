package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Pink100
import com.vtol.petpal.ui.theme.Pink50
import com.vtol.petpal.ui.theme.TextPurple
import com.vtol.petpal.util.toAgeString

@Composable
fun PetCard(
    pet: Pet,
    onScheduleClick: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onEditClicked: (String) -> Unit,
    task: TaskUi?,
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    val taskTxt = when (task?.type) {
        TaskType.FEED -> "🍽️ needs feeding today"
        TaskType.VET -> "🏥 has a vet appointment"
        TaskType.MEDICATION -> "💊 needs meds today"
        TaskType.WALK -> "🚶‍♂️ needs a walk today"
        else -> "has a task today"
    }

    val genderIcon = when (pet.gender) {
        PetGender.Male -> R.drawable.ic_male
        PetGender.Female -> R.drawable.ic_female
        PetGender.Unknown -> R.drawable.ic_pets_filled
    }

    Card(
        onClick = { onCardClick(pet.id) },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(context).data(pet.imagePath).crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.pet_placeholder),
                    error = painterResource(R.drawable.pet_placeholder),
                    contentScale = ContentScale.Crop,
                    contentDescription = "pet photo"
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = pet.petName,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = TextPurple
                        )

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Pink50)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = pet.specie.ifEmpty { "Unspecified" },
                                fontSize = 12.sp,
                                color = Pink100
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(genderIcon),
                            contentDescription = null
                        )

                        Text(
                            text = pet.birthDate.toAgeString(),
                            fontSize = 13.sp,
                            color = Color.LightGray
                        )
                    }

                    val list = mutableListOf(pet.breed.ifEmpty { "Unspecified" })
                    if (task != null) {
                        list.add(taskTxt)
                    }
                    FlippingText(texts = list)
                }
            }
            Spacer(modifier = Modifier.height(26.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = ExtraLightPurple)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onEditClicked(pet.id) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "",
                        tint = MainPurple
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Edit",
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontSize = 16.sp,
                        color = MainPurple
                    )
                }


                VerticalDivider(modifier = Modifier.fillMaxHeight(), color = ExtraLightPurple)

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onScheduleClick(pet.id) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(R.drawable.ic_calendar_outlined),
                        contentDescription = "",
                        tint = MainPurple
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Schedule",
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontSize = 16.sp,
                        color = MainPurple
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun CardPreview() {
    PetPalTheme {

        PetCard(
            pet = Pet(
                id = "pet_001",
                petName = "Buddy",
                breed = "Golden Retriever",
                specie = "Dog",
                gender = PetGender.Female,
                personality = listOf("Friendly", "Playful"),
            ),
            onScheduleClick = {},
            onCardClick = {},
            onEditClicked = {},
            task = null
        )
    }
}