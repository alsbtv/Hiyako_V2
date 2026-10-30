package com.hiyako.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val themeColor by viewModel.themeColor.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115))) {
        TopAppBar(
            title = { Text("Settings", color = Color.White) },
            navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text("App Theme", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Red", "Green", "Purple", "Blue").forEach { color ->
                    Button(
                        onClick = { viewModel.setTheme(color) }, 
                        colors = ButtonDefaults.buttonColors(containerColor = if (themeColor == color) MaterialTheme.colorScheme.primary else Color.DarkGray)
                    ) {
                        Text(color, color = Color.White)
                    }
                }
            }
        }
    }
}
