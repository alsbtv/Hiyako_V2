package com.hiyako.app.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController) {
    var contentType by remember { mutableStateOf(setOf<String>()) }
    var rating by remember { mutableStateOf(setOf<String>()) }
    var country by remember { mutableStateOf(setOf<String>()) }
    var release by remember { mutableStateOf(setOf<String>()) }
    var season by remember { mutableStateOf(setOf<String>()) }
    var genres by remember { mutableStateOf(setOf<String>()) }
    var tags by remember { mutableStateOf(setOf<String>()) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115))) {
        // Top Bar
        TopAppBar(
            title = { Text("Filter By", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            actions = {
                TextButton(onClick = {
                    contentType = emptySet(); rating = emptySet(); country = emptySet()
                    release = emptySet(); season = emptySet(); genres = emptySet(); tags = emptySet()
                }) {
                    Text("Clear All", color = MaterialTheme.colorScheme.primary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
            item { FilterSection("Content Type", listOf("TV", "Movie", "Ona", "Ova", "Special"), contentType) { contentType = it } }
            item { FilterSection("Content Rating", listOf("G", "PG", "PG-13", "R", "R+"), rating) { rating = it } }
            item { FilterSection("Country Of Origin", listOf("Japan", "China", "Korea", "Taiwan"), country) { country = it } }
            item { FilterSection("Release", listOf("On Going", "Completed", "Hiatus", "Coming Soon"), release) { release = it } }
            item { FilterSection("Season", listOf("Winter", "Spring", "Summer", "Fall"), season) { season = it } }
            item { FilterSection("Genres", listOf("Action", "Adventure", "Comedy", "Drama", "Fantasy", "Horror", "Mystery", "Romance", "Sci-Fi", "Slice of Life", "Sports", "Supernatural", "Thriller"), genres) { genres = it } }
            item { FilterSection("Tags", listOf("Isekai", "School", "Military", "Music", "Psychological", "Space", "Time Travel", "Vampire"), tags) { tags = it } }
        }

        // Apply Button (floating at bottom)
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun FilterSection(title: String, options: List<String>, selected: Set<String>, onSelectionChange: (Set<String>) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) { option ->
                FilterChip(
                    selected = option in selected,
                    onClick = { onSelectionChange(if (option in selected) selected - option else selected + option) },
                    label = { Text(option, color = Color.White, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF1E1E1E),
                        selectedContainerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}
