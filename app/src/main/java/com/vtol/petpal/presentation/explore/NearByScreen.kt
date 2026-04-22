package com.vtol.petpal.presentation.explore

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.vtol.petpal.presentation.explore.components.CategoryList
import com.vtol.petpal.presentation.explore.components.LoadingIndicator
import com.vtol.petpal.presentation.explore.components.LocationList
import com.vtol.petpal.ui.theme.PetPalTheme
import kotlinx.coroutines.launch
import timber.log.Timber


@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun NearByScreenContent() {

    val viewModel: NearViewModel = hiltViewModel()

    val cameraPositionState = rememberCameraPositionState()


    val location by viewModel.location.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var isLoaded by remember { mutableStateOf(false) }


    val vets by viewModel.locations.collectAsState()

    LaunchedEffect(location) {
        location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 13f)
        }
    }

    val coroutineScope = rememberCoroutineScope()


    var selectedVet by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(initialPage = selectedVet) {
        vets.size
    }

    // maybe i could let listen to the selected VEt index instead of pager state for smoother movement
    LaunchedEffect(pagerState.currentPage) {
        if (vets.isNotEmpty()) {
            val vet = vets[pagerState.currentPage]
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(vet.lat, vet.lng), 13f),
                durationMs = 500
            )
        }
    }



    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 16.dp)
    ) {

        location?.let { location ->
            val cLocation = remember { MarkerState(LatLng(location.latitude, location.longitude)) }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isLoaded = true }
            ) {
                // first display the user location

                Marker(
                    state = cLocation,
                )


                vets.forEachIndexed { index, vet ->
                    Timber.tag("Address").v("ITs: ${vet.address}")

                    // then shows the near selected locations

                    Marker(state = MarkerState(LatLng(vet.lat, vet.lng)), onClick = {
                        selectedVet = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        true
                    })


                }
            }

            AnimatedVisibility(
                visible = !isLoaded,
                exit = fadeOut()
            ) {
                LoadingIndicator()
            }

            if (isLoaded) {
                CategoryList(
                    Modifier
                        .align(Alignment.TopStart),
                    selectedCategory,
                    onCategoryClicked = {
                        viewModel.onCategorySelected(it)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )

                LocationList(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    vets,
                    pagerState
                )

            }
        }
    }
}


@Composable
fun NearByScreen() {
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(true) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    when {
        hasLocationPermission -> {
            NearByScreenContent()
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    text = "Please grant location permission to see nearby places"
                )
            }
        }
    }
}


@Preview
@Composable
fun NearByScreenPreview() {
    PetPalTheme {
        NearByScreen()
    }
}