package com.hiyako.app.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(title: String, posterUrl: String, navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var episodeSearchQuery by remember { mutableStateOf("") }
    val tabs = listOf("Details", "Episodes", "Watch History", "Favorite Episodes", "Gallery")

    // Test video URL for now
    val testVideoUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

    fun playVideo() {
        navController.navigate("player/${android.net.Uri.encode(testVideoUrl)}")
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115))) {
        AsyncImage(model = posterUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(400.dp).blur(20.dp).alpha(0.6f))
        Box(modifier = Modifier.fillMaxWidth().height(400.dp).background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF0F1115)))))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                    Row {
                        IconButton(onClick = { }, modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)) { Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White) }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { }, modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)) { Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White) }
                    }
                }
                
                Spacer(modifier = Modifier.height(50.dp))
                
                Row(modifier = Modifier.padding(16.dp)) {
                    AsyncImage(model = posterUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.width(120.dp).height(180.dp).clip(RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("TV - Fall 2026", color = Color.LightGray, fontSize = 12.sp)
                        Text("4 Episode", color = Color.LightGray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text("On Going", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { playVideo() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).height(45.dp)) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                        Text("Play Now", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.StarBorder, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tabs.size) { index ->
                        val tab = tabs[index]
                        Box(modifier = Modifier.background(if (index == selectedTab) MaterialTheme.colorScheme.primary else Color(0xFF1E1E1E), RoundedCornerShape(8.dp)).clickable { selectedTab = index }.padding(horizontal = 16.dp, vertical = 10.dp)) {
                            Text(tab, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (selectedTab == 0) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).background(Color(0xFFE50046), RoundedCornerShape(12.dp)).padding(16.dp)) {
                        Text("This anime may contain sexual, violence, or mature scenes that are not suitable for children.", color = Color.White, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Title", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(title, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Story", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("An epic story about $title.", color = Color.LightGray)
                    }
                }
            } else if (selectedTab == 1) {
                item {
                    OutlinedTextField(
                        value = episodeSearchQuery,
                        onValueChange = { episodeSearchQuery = it },
                        placeholder = { Text("Search episode...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp).clip(RoundedCornerShape(8.dp)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color(0xFF1E1E1E), focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent)
                    )
                }
                items(15) { index ->
                    val epNum = index + 1
                    if (episodeSearchQuery.isEmpty() || epNum.toString().contains(episodeSearchQuery)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = posterUrl, contentDescription = null, modifier = Modifier.width(100.dp).height(60.dp).clip(RoundedCornerShape(4.dp)), contentScale = ContentScale.Crop)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Episode $epNum", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Button(onClick = { playVideo() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                                Text("Play Now", color = Color.White)
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}
