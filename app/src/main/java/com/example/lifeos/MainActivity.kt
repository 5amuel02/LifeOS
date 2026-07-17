package com.example.lifeos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.content.ContextCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.lifeos.core.strings.AppLanguage
import com.example.lifeos.core.strings.EnglishStrings
import com.example.lifeos.core.strings.IndonesianStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.settings.ThemeMode
import com.example.lifeos.navigation.BottomNavBar
import com.example.lifeos.navigation.LifeOSNavHost
import com.example.lifeos.ui.screens.settings.SettingsViewModel
import com.example.lifeos.ui.theme.LifeOSTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* user choice respected */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        setContent {
            LifeOSRoot()
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
private fun LifeOSRoot() {
    // The OS splash is static on Android 11 and below, so it hands off to this
    // in-app overlay (same colors and arc) that actually spins, then fades out.
    var splashDone by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!splashDone) {
            delay(1100)
            splashDone = true
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LifeOSApp()
        AnimatedVisibility(
            visible = !splashDone,
            exit = fadeOut(animationSpec = tween(durationMillis = 400))
        ) {
            SplashLoadingOverlay()
        }
    }
}

@Composable
private fun SplashLoadingOverlay() {
    val backgroundColor = colorResource(R.color.splash_background)
    val spinnerColor = colorResource(R.color.splash_spinner)
    val transition = rememberInfiniteTransition(label = "splashSpin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1000, easing = LinearEasing)),
        label = "splashSpinAngle"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(178.dp)) {
            val strokeWidth = 20.dp.toPx()
            val inset = strokeWidth / 2f
            rotate(angle) {
                drawArc(
                    color = spinnerColor,
                    startAngle = -90f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
fun LifeOSApp() {
    val settingsViewModel: SettingsViewModel = viewModel()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    val systemInDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemInDarkTheme
    }
    val strings = when (settings.language) {
        AppLanguage.INDONESIAN -> IndonesianStrings
        AppLanguage.ENGLISH -> EnglishStrings
    }

    CompositionLocalProvider(LocalStrings provides strings) {
        LifeOSTheme(darkTheme = darkTheme, dynamicColor = settings.themeMode == ThemeMode.SYSTEM) {
            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { BottomNavBar(navController) }
            ) { innerPadding ->
                LifeOSNavHost(navController = navController, innerPadding = innerPadding)
            }
        }
    }
}
