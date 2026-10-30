package com.hiyako.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

// --- Mock Data Models ---
data class MockAnime(
    val title: String,
    val poster: String,
    val banner: String? = null,
    val episode: String,
    val rating: String,
    val status: String,
    val is18Plus: Boolean = false
)

val mockAnimes = listOf(
    MockAnime(
        "Bleach: Sennen Kessen-hen",
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx116674-2P4wLh56yH2W.jpg",
        "https://s4.anilist.co/file/anilistcdn/media/anime/banner/116674-ZXyP8fK5Jz8Z.jpg",
        "10",
        "8.5",
        "Completed",
        false
    ),
    MockAnime(
        "One Piece",
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx21-ELcVjS9gYpDp.jpg",
        null,
        "1180",
        "9.2",
        "On Going",
        false
    ),
    MockAnime(
        "Black Clover 2nd Season",
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx97940-T3YtV6y0s3L7.jpg",
        null,
        "4",
        "8.1",
        "Completed",
        true
    ),
    MockAnime(
        "Jujutsu Kaisen",
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx113415-bbBWj4pEFseh.jpg",
        null,
        "24",
        "8.8",
        "Completed",
        true
    ),
    MockAnime(
        "Dragon Ball Super",
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx21157-8W4zYt6y0s3L.jpg",
        null,
        "131",
        "8.0",
        "Completed",
        false
    )
)

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onMenuClick: () -> Unit) {
    val pagerState = rememberPagerState { 3 }
    
    // Auto scroll banner every 11 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(11000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % 3)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115))) {
        item {
            // Top Bar
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White) }
                Text("Browse Anime", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = { }) { Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White) }
                    IconButton(onClick = { }) { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) }
                }
            }

            // Hero Banner (Auto-scroll)
            Box(modifier = Modifier.fillMaxWidth().height(250.dp).padding(horizontal = 16.dp).clip(RoundedCornerShape(16.dp))) {
                HorizontalPager(state = pagerState) { page ->
                    val anime = mockAnimes[page]
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = anime.banner ?: anime.poster,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC0F1115)))))
                        Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                            Text(anime.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                            Text("Episode ${anime.episode}", color = Color.LightGray, fontSize = 14.sp)
                        }
                    }
                }
                // Dots indicator
                Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { index ->
                        Box(modifier = Modifier.size(if (index == pagerState.currentPage) 8.dp else 6.dp).background(if (index == pagerState.currentPage) Color(0xFFE50046) else Color.Gray, RoundedCornerShape(50)))
                    }
                }
            }
        }

        // Sections
        item {
            SectionHeader("Continue Watching")
            LazyRow(contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mockAnimes.take(3)) { anime ->
                    ContinueWatchingCard(anime)
                }
            }
        }

        item {
            SectionHeader("Recent Releases")
            LazyRow(contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mockAnimes) { anime ->
                    AnimeCard(anime)
                }
            }
        }

        item {
            SectionHeader("Most Popular")
            LazyRow(contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mockAnimes.reversed()) { anime ->
                    AnimeCard(anime)
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(80.dp)) } // Padding for bottom nav
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("View More", color = Color.LightGray, fontSize = 12.sp, modifier = Modifier.clickable { })
    }
}

@Composable
fun AnimeCard(anime: MockAnime) {
    Column(modifier = Modifier.width(130.dp)) {
        Box(modifier = Modifier.height(180.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))) {
            AsyncImage(model = anime.poster, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            
            // Top Left Badge (Status)
            Box(modifier = Modifier.align(Alignment.TopStart).padding(4.dp).background(if (anime.status == "On Going") Color(0xFF4CAF50) else Color(0xFFE50046), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp)) {
                Text(anime.status, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            
            // Top Right Badge (Age)
            if (anime.is18Plus) {
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(Color(0xFFE50046), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp)) {
                    Text("+17", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Bottom Left Badge (Episode)
            Box(modifier = Modifier.align(Alignment.BottomStart).padding(4.dp).background(Color(0xCC000000), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp)) {
                Text("Episode: ${anime.episode}", color = Color.White, fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(anime.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(12.dp))
            Text(" ${anime.rating} (1.3M Vote)", color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun ContinueWatchingCard(anime: MockAnime) {
    Row(modifier = Modifier.width(250.dp).background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(80.dp).height(60.dp).clip(RoundedCornerShape(4.dp))) {
            AsyncImage(model = anime.poster, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp).background(Color(0xCC000000), RoundedCornerShape(2.dp)).padding(horizontal = 2.dp)) {
                Text("24:00", color = Color.White, fontSize = 8.sp)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Episode ${anime.episode}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(anime.title, color = Color.LightGray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = 0.6f, modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)), color = Color(0xFFE50046), trackColor = Color.DarkGray)
        }
    }
}
