package com.vtol.petpal.presentation.nearby

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.vtol.petpal.R
import com.vtol.petpal.presentation.nearby.components.CategoryList
import com.vtol.petpal.presentation.nearby.components.LoadingIndicator
import com.vtol.petpal.presentation.nearby.components.LocationList
import com.vtol.petpal.ui.theme.PetPalTheme
import kotlinx.coroutines.launch


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



    Box(modifier = Modifier.fillMaxSize()) {

        location?.let { location ->
            val cLocation = remember { MarkerState(LatLng(location.latitude, location.latitude)) }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isLoaded = true }
            ) {
                // first display the user location

                Marker(
                    state = cLocation,
                    icon = vectorToBitmapDescriptor(R.drawable.profile_img)
                )


                vets.forEachIndexed { index, vet ->
                    Log.v("Address", "ITs: ${vet.address}")

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
fun vectorToBitmapDescriptor(@DrawableRes resId: Int): BitmapDescriptor {
    val imageBitmap = ImageBitmap.imageResource(resId)
    val bitmap = imageBitmap.asAndroidBitmap()
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}


@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun NearByScreen() {
    val context = LocalContext.current

    var hasLocationPermission by remember { mutableStateOf(false) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (!hasLocationPermission) {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Request permission when the composable enters the composition
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    if (hasLocationPermission) {
        NearByScreenContent()
    } else {
        Text("Please grant location permission to see nearby places")
    }
}


@SuppressLint("MissingPermission")
@Preview
@Composable
fun NearByScreenPreview() {
    PetPalTheme {
        NearByScreen()
    }
}