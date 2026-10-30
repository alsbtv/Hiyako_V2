package com.hiyako.app.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PlayerScreen(videoUrl: String, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        Text("Video Player (MPV will be integrated here)\nURL: $videoUrl", color = Color.White, fontSize = 14.sp)
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
    }
}
