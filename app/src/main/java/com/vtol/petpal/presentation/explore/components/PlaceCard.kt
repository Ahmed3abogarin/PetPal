package com.vtol.petpal.presentation.explore.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.libraries.places.api.model.OpeningHours
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.map.OpeningStatus
import com.vtol.petpal.domain.model.map.PlaceAddress
import com.vtol.petpal.ui.theme.ButtonLightGray
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.Gold
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.toFormattedDistance

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    place: PlaceAddress,
    onDirectionsClicked: (PlaceAddress) -> Unit,
    onCallClicked: (String) -> Unit,
    onBookClick: (Uri) -> Unit
) {
    val statusText = when {
        place.openingStatus.is24Hours -> "24 hours"
        place.isOpen == true && place.openingStatus.closingTime != null ->
            "until ${place.openingStatus.closingTime}"
        place.isOpen == false && place.openingStatus.nextOpeningTime != null ->
            "Opens ${place.openingStatus.nextOpeningDay ?: ""} ${place.openingStatus.nextOpeningTime}".trim()
        else -> "Unknown"
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(width = 0.5.dp, color = ExtraLightPurple, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(12.dp)

    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            val context = LocalContext.current

            // TODO: Update the place holder image
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1 / 1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(context).data(place.photo).build(),
                contentDescription = "",
                placeholder = painterResource(R.drawable.location_img),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))


            // Place Name + Status + openings hours + Distance + rating + reviews
            Column {
                Text(
                    text = place.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    place.isOpen?.let {
                        Text(
                            text = if (it) "Open" else "Closed",
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (it) Color.Green else Color.Red
                        )
                    }
                    // TODO:
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "$statusText  •  ${place.distance?.toFormattedDistance()}",
                        fontSize = 8.sp,
                        color = Color.Gray
                    )

                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(16.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Gold
                    )

                    Text(
                        text = place.rating.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 8.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "(${place.totalRating})",
                        fontSize = 7.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Actions buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            PlaceButton(
                modifier = Modifier.weight(1f),
                txt = "Directions",
                icon = R.drawable.ic_location,
                onClick = { onDirectionsClicked(place) }
            )
            PlaceButton(
                modifier = Modifier.weight(1f),
                txt = "Call",
                enabled = place.phoneNumber != null,
                icon = R.drawable.ic_phone,
                onClick = {
                    place.phoneNumber?.let {
                        onCallClicked(it)

                    }
                }
            )
            PlaceButton(
                modifier = Modifier.weight(1f),
                txt = "Book",
                enabled = place.url != null,
                icon = R.drawable.ic_calendar_outlined,
                onClick = {
                    place.url?.let {
                        onBookClick(it)
                    }
                }
            )
        }
    }
}

@Composable
fun PlaceButton(
    modifier: Modifier = Modifier,
    icon: Int,
    txt: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled) { onClick() }
            .background(if (enabled) CellsBgPurple else ButtonLightGray)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val color = if (enabled) MainPurple else Color.Gray
        Icon(
            modifier = Modifier.size(14.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = txt,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}


@Preview
@Composable
fun PlaceCardPreview() {
    PetPalTheme {
        PlaceCard(
            onCallClicked = {},
            onBookClick = {},
            onDirectionsClicked = {},
            place = PlaceAddress(
                url = "".toUri(),
                openingHours = OpeningHours.builder().build(),
                rating = 4.7,
                name = "TailWaggers Clinic",
                lat = 0.0,
                lng = 0.0,
                phoneNumber = "+20123456789",
                isOpen = true,
                distance = 0.0f,
                totalRating = 0,
                photo = null,
                id = "",
                openingStatus = OpeningStatus("10:00 AM", false, "10:00 AM", "Tomorrow")
            )
        )
    }
}