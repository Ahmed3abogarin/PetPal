package com.vtol.petpal.presentation.nearby.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Vet
import com.vtol.petpal.ui.theme.MainPurple


@Composable
fun LocationList(modifier: Modifier,locationList: List<Vet>, pagerState: PagerState) {

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 42.dp)
    ) {
        LocationCard(locationList[it])
    }

}

@Composable
fun LocationCard(vet: Vet) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(200.dp)
            .width(290.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(12.dp),
        onClick = {
            Toast.makeText(context,vet.name, Toast.LENGTH_SHORT).show()
        }
    ) {
        Column {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(R.drawable.location_img),
                contentDescription = "location image"
            )

            Text(
                modifier = Modifier.padding(start = 10.dp, top = 8.dp),
                fontWeight = FontWeight.Bold,
                text = vet.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "open util 9:00 AM", color = MainPurple)

                Row {
                    Icon(Icons.Default.Star, tint = Color.Yellow, contentDescription = "")
                    Text(text = "5.5", color = Color.Black)

                }

            }
        }


    }

}

//@Preview
//@Composable
//fun LocationListPreview() {
//    val list = listOf(
//        Vet("", 4.00, 6.00, "")
//    )
//    PetPalTheme {
//        LocationList(Modifier,list)
//    }
//}