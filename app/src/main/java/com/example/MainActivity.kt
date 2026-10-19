package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.MikaScreen
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.AnimationScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MikaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MikaApp()
            }
        }
    }
}

@Composable
fun MikaApp(
    viewModel: MikaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = uiState.currentScreen != MikaScreen.HOME) {
        viewModel.setScreen(MikaScreen.HOME)
    }

    val showBottomBar = uiState.currentScreen == MikaScreen.HOME || 
                        uiState.currentScreen == MikaScreen.PROFILE

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MikaDarkBg),
        contentWindowInsets = if (uiState.currentScreen == MikaScreen.CHAT || uiState.currentScreen == MikaScreen.ANIMATION) {
            WindowInsets(0, 0, 0, 0)
        } else {
            ScaffoldDefaults.contentWindowInsets
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentScreen = uiState.currentScreen,
                    onScreenSelected = { screen ->
                        viewModel.setScreen(screen)
                    },
                    onCenterLogoClick = {
                        viewModel.setScreen(MikaScreen.CHAT)
                    },
                    isListening = uiState.isSendingMessage
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MikaDarkBg)
        ) {
            Crossfade(
                targetState = uiState.currentScreen,
                animationSpec = tween(280),
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    MikaScreen.HOME -> {
                        HomeScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                    MikaScreen.ANIMATION -> {
                        AnimationScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    MikaScreen.CHAT -> {
                        ChatScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = innerPadding.calculateTopPadding())
                        )
                    }
                    MikaScreen.PROFILE -> {
                        ProfileScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                    MikaScreen.CALL -> {
                        HomeScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
