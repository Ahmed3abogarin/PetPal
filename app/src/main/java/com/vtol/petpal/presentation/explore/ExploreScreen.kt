package com.vtol.petpal.presentation.explore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.map.PlaceCategory
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.explore.components.CategoryList
import com.vtol.petpal.presentation.explore.components.GPSNotGrantedScreen
import com.vtol.petpal.presentation.explore.components.PlaceCard
import com.vtol.petpal.presentation.explore.components.PlaceCardShimmer
import com.vtol.petpal.presentation.explore.components.PlaceMarker
import com.vtol.petpal.presentation.explore.components.UserLocationMarker
import com.vtol.petpal.presentation.explore.util.MapsIntentHelper.openGoogleMaps
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.cardColors
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.ShareManager.openDialer
import com.vtol.petpal.util.ShareManager.openWebsite
import com.vtol.petpal.util.rememberShimmerBrush
import com.vtol.petpal.util.showToast


@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun ExploreScreenContent(
    onCategoryClicked: (PlaceCategory) -> Unit,
    state: UiState
) {

    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current


    LaunchedEffect(state.location) {
        state.location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 13f)
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        // screen header
        item {
            Column(
                modifier = Modifier
                    .background(petPalGradient)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explore",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    AppIconButton(
                        icon = R.drawable.ic_search,
                    ) {
                        context.showToast("Search is not available yet")
                    }
                }

                CategoryList(
                    enabled = !state.isLoading,
                    modifier = Modifier.padding(start = 16.dp),
                    onCategoryClicked = {
                        onCategoryClicked(it)
                    },
                    selectCategory = state.category
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))
        }


        item {
            var mapLoading by remember { mutableStateOf(true) }

            // Maps window
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 4.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(petPalGradient)
                    .height(252.dp)
                    .fillMaxWidth()
            ) {
                state.location?.let { location ->
                    val cLocation = remember(location) { // ← add location as key
                        MarkerState(LatLng(location.latitude, location.longitude))
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapLoaded = {
                            mapLoading = false
                        }
                    ) {
                        // first display the user location
                        UserLocationMarker(state = cLocation)

                        state.locations.forEach { place ->
                            // then shows the near selected locations
                            PlaceMarker(
                                state = MarkerState(LatLng(place.lat, place.lng)),
                                place = place
                            )
                        }
                    }
                }

                if (mapLoading) {
                    val brush = rememberShimmerBrush(cardColors)
                    Box(modifier = Modifier.fillMaxSize().background(brush))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        when {
            state.isLoading -> {
                item {
                    PlaceCardShimmer(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }

            state.error != null -> {
                item {
                    LaunchedEffect(state.error) {
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            else -> {

                items(state.locations) { place ->
                    PlaceCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 12.dp),
                        onCallClicked = {
                            openDialer(context, it)
                        },
                        onBookClick = {
                            openWebsite(context, it)
                        },
                        onDirectionsClicked = {
                            openGoogleMaps(context, it)
                        },
                        place = place
                    )
                }

                if (state.locations.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No places found")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ExploreScreen(
    state: UiState,
    onCategoryClicked: (PlaceCategory) -> Unit,
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var showRationale by remember { mutableStateOf(false) }
    var permanentlyDenied by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        when {
            granted -> {
                hasLocationPermission = true
                showRationale = false
                permanentlyDenied = false
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showRationale = true
            }
            else -> {
                permanentlyDenied = true
            }
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        hasLocationPermission = granted
        if (granted) {
            permanentlyDenied = false
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            onPermissionGranted()
        }
    }

    when {
        hasLocationPermission -> {
            ExploreScreenContent(
                onCategoryClicked = onCategoryClicked,
                state = state
            )
        }

        showRationale -> {
            GPSNotGrantedScreen {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        permanentlyDenied -> {
            GPSNotGrantedScreen(
                buttonTxt = "Open settings",
                message = "Location permission was permanently denied. Please enable it in Settings."
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                settingsLauncher.launch(intent)
            }
        }

        else -> {
            GPSNotGrantedScreen {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
}


@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Preview
@Composable
fun ExploreScreenPreview() {
    PetPalTheme {
        ExploreScreenContent(state = UiState(), onCategoryClicked = {})
    }
}