package com.hiyako.app.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    val quickFilters = listOf("TV", "Movie", "Ova", "Action", "Romance", "Isekai")

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115)).padding(16.dp)) {
        Text("Search", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search anime...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { navController.navigate("filter") },
                modifier = Modifier.background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(quickFilters) { filter ->
                AssistChip(
                    onClick = { },
                    label = { Text(filter, color = Color.White, fontSize = 12.sp) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF1E1E1E))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val mockResults = List(12) { "Anime Result $it" }
        LazyVerticalGrid(columns = GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(mockResults) { item ->
                Column(modifier = Modifier.clickable {
                    navController.navigate("details/${java.net.URLEncoder.encode(item, "UTF-8")}/${java.net.URLEncoder.encode("https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx116674-2P4wLh56yH2W.jpg", "UTF-8")}")
                }) {
                    AsyncImage(
                        model = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx116674-2P4wLh56yH2W.jpg",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(150.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))
                    )
                    Text(item, color = Color.White, fontSize = 10.sp, maxLines = 2, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
