package com.hiyako.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hiyako.app.data.model.ScrapedAnime
import com.hiyako.app.ui.screens.DetailsScreen
import com.hiyako.app.ui.screens.HomeScreen
import com.hiyako.app.ui.theme.DarkBg
import com.hiyako.app.ui.theme.HiyakoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiyakoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
                    val navController = rememberNavController()
                    var selectedAnime by remember { mutableStateOf<ScrapedAnime?>(null) }

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(onAnimeClick = { anime ->
                                selectedAnime = anime
                                navController.navigate("details")
                            })
                        }
                        composable("details") {
                            selectedAnime?.let { anime ->
                                DetailsScreen(anime = anime, onBack = {
                                    navController.popBackStack()
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}
