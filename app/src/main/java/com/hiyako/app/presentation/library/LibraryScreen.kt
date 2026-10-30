package com.hiyako.app.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(onMenuClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Watch History", "My List", "Custom Lists", "Downloads", "Offline")

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Top Bar
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White) }
            Text("Library", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Row {
                IconButton(onClick = { }) { Icon(Icons.Default.Cast, contentDescription = null, tint = Color.White) }
                IconButton(onClick = { }) { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) }
            }
        }

        // Tabs
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tabs) { tab ->
                val index = tabs.indexOf(tab)
                Box(
                    modifier = Modifier
                        .background(if (index == selectedTab) MaterialTheme.colorScheme.primary else Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                        .clickable { selectedTab = index }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(tab, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Sort Header
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Sort, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("by Name", color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mock Grid
        val mockAnime = listOf("Anime 1", "Anime 2", "Anime 3", "Anime 4", "Anime 5", "Anime 6")
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockAnime) { name ->
                Column {
                    Box(modifier = Modifier.height(220.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))) {
                        AsyncImage(
                            model = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx116674-2P4wLh56yH2W.jpg",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(modifier = Modifier.align(Alignment.TopStart).padding(4.dp).background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("On Going", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp).background(Color(0xCC000000), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("TV - 2026", color = Color.White, fontSize = 10.sp)
                        }
                    }
                    Text(name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                    Text("★ 8.5 (1.3M Vote)", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}
