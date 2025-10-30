package com.vtol.petpal.presentation.nearby

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.vtol.petpal.ui.theme.PetPalTheme


@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun NearByScreenContent() {
    val application = LocalContext.current.applicationContext as Application

    val cameraPositionState = rememberCameraPositionState()



    val viewModel: NearViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NearViewModel(application) as T
            }
        }
    )

    val location by viewModel.location.collectAsState()


    val vets by viewModel.vets.collectAsState()

    LaunchedEffect(location) {
        location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 13f)
        }
    }

//    val context = LocalContext.current
//    var hasLocationPermission by remember { mutableStateOf(false) }
//    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        hasLocationPermission = granted
//        if (granted) getCurrentLocation(context) { lat, lng ->
//            currentLatLng = LatLng(lat, lng)
//        }
//    }

//    LaunchedEffect(Unit) {
//        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//    }
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
//    }
//
//    val markerState = remember { MarkerState(position = currentLatLng) }




    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        vets.forEach {
            Log.v("gjkdsghs","ITs: ${it.address}")
            Marker(state = MarkerState(LatLng(it.lat,it.lng)),)
        }
//        if (hasLocationPermission) {
//            Marker(state = markerState, title = "You are here")
//        }


    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context,onLocation: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocation(location.latitude, location.longitude)
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun NearByScreen() {
    val context = LocalContext.current

    var hasLocationPermission by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

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