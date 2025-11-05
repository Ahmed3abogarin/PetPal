package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.vtol.petpal.ui.theme.ButtonLightGray
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.toAgeString

@Composable
fun PetCard(pet: Pet, onScheduleClick: (String) -> Unit, onCardClick: (String) -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = { onCardClick(pet.id) },
        colors = CardDefaults.cardColors(containerColor = LightPurple),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(context).data(pet.imagePath).build(),
                    placeholder = painterResource(R.drawable.cat),
                    error = painterResource(R.drawable.cat) ,
                    contentDescription = "pet photo"
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = pet.petName, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "${pet.birthDate.toAgeString()} • ${pet.breed}",
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )
                    Text(text = "\uD83D\uDC8A Needs meds today")
                }
            }

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonLightGray),
                onClick = {
                    onScheduleClick(pet.id)
                }) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(26.dp),
                        painter = painterResource(R.drawable.ic_calender),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Schedule",
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = Color.Black
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun PetCardPreview() {
    PetPalTheme {
        PetCard(Pet(), onCardClick = {}, onScheduleClick = {})
    }

}