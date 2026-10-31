package com.hiyako.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hiyako.app.data.model.ScrapedAnime
import com.hiyako.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DetailsScreen(
    anime: ScrapedAnime,
    onBack: () -> Unit,
    viewModel: DetailsViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Details", "Episodes", "Watch History", "Gallery")
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val episodes by viewModel.episodes.collectAsState()
    val galleryImages by viewModel.galleryImages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // منطق تغيير البوستر كل 5 ثوانٍ
    var currentPosterIndex by remember { mutableIntStateOf(0) }
    val allPosters = remember(galleryImages, anime) {
        val list = mutableListOf(anime.posterUrl ?: "")
        list.addAll(galleryImages)
        list.filter { it.isNotBlank() }
    }

    LaunchedEffect(allPosters) {
        if (allPosters.size > 1) {
            while (true) {
                delay(5000)
                currentPosterIndex = (currentPosterIndex + 1) % allPosters.size
            }
        }
    }

    LaunchedEffect(anime) {
        viewModel.loadDetails(anime)
    }

    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(anime.bannerUrl ?: anime.posterUrl).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(400.dp).blur(20.dp).alpha(0.6f)
        )
        Box(modifier = Modifier.fillMaxWidth().height(400.dp).background(Brush.verticalGradient(listOf(Color.Transparent, DarkBg))))

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 32.dp)) {
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) { Text("✕", color = TextPrimary, fontSize = 24.sp) }
                    Row {
                        Text("🔔", fontSize = 24.sp, modifier = Modifier.padding(end = 16.dp))
                        Text("♡", fontSize = 24.sp)
                    }
                }
            }

            item {
                Row(modifier = Modifier.padding(16.dp).padding(top = 8.dp)) {
                    // البوستر المتغير
                    AsyncImage(
                        model = if (allPosters.isNotEmpty()) allPosters[currentPosterIndex] else anime.posterUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.width(120.dp).height(180.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        // اسم الأنمي - عند الضغط عليه ينسخ
                        Text(
                            text = anime.title,
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.clickable {
                                clipboardManager.setText(AnnotatedString(anime.title))
                                android.widget.Toast.makeText(context, "Title Copied!", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(anime.status ?: "N/A", color = TextSecondary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${anime.episodeNumber ?: "?"} Episodes", color = TextSecondary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Box(modifier = Modifier.background(if (anime.status == "RELEASING") OnGoingGreen else AccentRed, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(if (anime.status == "RELEASING") "On Going" else "Completed", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.background(MalBlue, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                                    Text("MAL", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Text(anime.malScore ?: "N/A", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.background(AccentRed, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                                    Text("Ani", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Text(anime.score ?: "N/A", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f).height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed), // يمكن تغييرها لـ OnGoingGreen إذا كان هناك سجل مشاهدة
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("▶ Play Now", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("🔖", fontSize = 24.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("⭐", fontSize = 24.sp, color = TextPrimary)
                }
            }

            item {
                LazyRow(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tabs.size) { index ->
                        val isSelected = selectedTab == index
                        Box(modifier = Modifier.background(if (isSelected) OnGoingGreen else CardSurface, RoundedCornerShape(16.dp)).padding(horizontal = 16.dp, vertical = 8.dp).clickable { selectedTab = index }) {
                            Text(tabs[index], color = TextPrimary, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            if (isLoading) {
                item { Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = AccentRed) } }
            } else {
                when (selectedTab) {
                    0 -> { // Details
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Box(modifier = Modifier.fillMaxWidth().background(AccentRed, RoundedCornerShape(12.dp)).padding(16.dp)) {
                                    Text("This anime may contain sexual, violence, or mature scenes that are not suitable for children or family viewing. Viewer discretion is advised.", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text("Title", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(anime.title, color = TextSecondary, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Story", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(anime.synopsis ?: "No synopsis available.", color = TextSecondary, fontSize = 14.sp)
                            }
                        }
                    }
                    1 -> { // Episodes
                        items(episodes) { ep ->
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { /* Play Episode */ }, verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.width(100.dp).height(60.dp).background(Color.DarkGray, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                    Text("EP ${ep.number}", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(ep.title ?: "Episode ${ep.number}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(modifier = Modifier.background(AccentRed, RoundedCornerShape(4.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    Text("Play", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    3 -> { // Gallery
                        items(galleryImages.chunked(2)) { rowImages ->
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowImages.forEach { imgUrl ->
                                    AsyncImage(
                                        model = imgUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.weight(1f).aspectRatio(0.7f).clip(RoundedCornerShape(8.dp))
                                    )
                                }
                                if (rowImages.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
