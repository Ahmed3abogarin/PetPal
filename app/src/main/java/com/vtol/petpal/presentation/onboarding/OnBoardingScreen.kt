package com.vtol.petpal.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState { onBoardingPages.size }

    val coroutine = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .padding(top = 16.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PetPalTopBar()

        // Scrollable horizontal image and texts
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            beyondViewportPageCount = 1
        ) { index ->
            val page = onBoardingPages[index]

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroSection(modifier = Modifier.weight(1f), showHearts = index == 0, img = page.img)

                CopyBlock(
                    titleLight = page.titleLight,
                    tileBold = page.titleBold,
                    description = page.description
                )
            }
        }


        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 76.dp, bottom = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val currentPage = pagerState.currentPage
            Text(
                modifier = Modifier
                    .alpha(if (currentPage != onBoardingPages.lastIndex) 1f else 0f)
                    .clickable(enabled = currentPage != onBoardingPages.lastIndex) { onFinish() }, text = "Skip"
            )

            PageIndicator(currentPage = currentPage, totalPages = pagerState.pageCount)


            FilledIconButton(
                onClick = {
                    if (currentPage == onBoardingPages.lastIndex) {
                        onFinish()
                    } else {
                        coroutine.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    }
                },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MainPurple)
            ) {
                Icon(
                    modifier = Modifier.rotate(180f),
                    painter = painterResource(R.drawable.ic_arrow),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun HeroSection(modifier: Modifier, showHearts: Boolean, img: Int) {
    Box(modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(340.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(MainPurple, Color.Transparent),
                    ),
                    shape = CircleShape,
                )
        )
        if (showHearts) {
            FloatingHeart(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-32).dp, y = 24.dp)
            )
            FloatingHeart(
                Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 8.dp)
            )
            FloatingHeart(
                Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-12).dp)
            )
        }

        Image(
            modifier = Modifier
                .offset(y = 18.dp)
                .height(500.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Crop,
            painter = painterResource(img),
            contentDescription = "onboarding image"
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.6f),
                            Color.White.copy(alpha = 0.98f),
                            Color.White
                        ),
                    )
                )
        )
    }
}

@Composable
fun PetPalTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            contentDescription = null,
            painter = painterResource(R.drawable.ic_logo)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "PetPal",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MainPurple
        )
    }
}

@Composable
private fun FloatingHeart(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = null,
        tint = MainPurple.copy(alpha = 0.45f),
        modifier = modifier.size(20.dp),
    )
}

@Composable
fun CopyBlock(
    modifier: Modifier = Modifier,
    tileBold: String,
    titleLight: String,
    description: String
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(tileBold) }
                append("\n$titleLight")
            },
            fontSize = 32.sp,
            lineHeight = 40.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = Color.Black,
        )
    }
}

@Composable
private fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalPages) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isActive) MainPurple else Color.LightGray)
                    .height(8.dp)
                    .width(if (isActive) 24.dp else 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun OnBoardingPreview() {
    PetPalTheme {
        OnboardingScreen {}
    }
}
