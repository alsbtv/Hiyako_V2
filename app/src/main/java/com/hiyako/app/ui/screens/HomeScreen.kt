package com.hiyako.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hiyako.app.data.model.ScrapedAnime
import com.hiyako.app.ui.components.AnimeBottomSheet
import com.hiyako.app.ui.components.AnimeCard
import com.hiyako.app.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onAnimeClick: (ScrapedAnime) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedNavItem by remember { mutableIntStateOf(0) }

    val trendingAnime by viewModel.trendingAnime.collectAsState()
    val latestAnime by viewModel.latestAnime.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // حالة النافذة المنسدلة
    var selectedAnimeForSheet by remember { mutableStateOf<ScrapedAnime?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = DrawerBg, modifier = Modifier.width(300.dp)) {
                Box(modifier = Modifier.height(180.dp).fillMaxWidth().background(AccentRed)) {
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Box(modifier = Modifier.size(64.dp).background(Color.White, CircleShape))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("alsb_tv", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                val drawerItems = listOf("Home", "Search", "Pick Random", "Recent Releases", "Library", "Settings")
                LazyColumn {
                    items(drawerItems) { item ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("●", color = AccentRed, modifier = Modifier.padding(end = 16.dp))
                            Text(item, color = TextPrimary, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = DarkBg,
            bottomBar = {
                NavigationBar(containerColor = CardSurface, modifier = Modifier.height(64.dp)) {
                    val navItems = listOf("Home", "Library", "Profile")
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedNavItem == index,
                            onClick = { selectedNavItem = index },
                            icon = { Text(if (item == "Home") "🏠" else if (item == "Library") "📚" else "👤", fontSize = 20.sp) },
                            label = { Text(item, color = if (selectedNavItem == index) AccentRed else TextSecondary, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp)) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("☰", fontSize = 24.sp, color = TextPrimary, modifier = Modifier.clickable { scope.launch { drawerState.open() } })
                            Text("Browse Anime", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Row {
                                Text("🔔", fontSize = 20.sp, modifier = Modifier.padding(end = 16.dp))
                                Text("🔍", fontSize = 20.sp)
                            }
                        }
                    }

                    if (isLoading) {
                        item { Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = AccentRed) } }
                    } else if (trendingAnime.isNotEmpty()) {
                        item {
                            val pagerState = rememberPagerState { trendingAnime.size.coerceAtLeast(1) }
                            LaunchedEffect(Unit) {
                                while (true) { delay(11000); if (trendingAnime.size > 1) pagerState.animateScrollToPage((pagerState.currentPage + 1) % trendingAnime.size) }
                            }
                            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                                HorizontalPager(state = pagerState, contentPadding = PaddingValues(horizontal = 16.dp), pageSpacing = 16.dp) { page ->
                                    val anime = trendingAnime.getOrNull(page) ?: return@HorizontalPager
                                    Box(modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp)).clickable { selectedAnimeForSheet = anime }) {
                                        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(anime.bannerUrl ?: anime.posterUrl).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
                                        Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                                            Text(anime.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                            Text("Episode ${anime.episodeNumber ?: "?"}", color = TextSecondary, fontSize = 14.sp)
                                        }
                                    }
                                }
                                Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    repeat(trendingAnime.size.coerceAtMost(5)) { index ->
                                        Box(modifier = Modifier.size(if (pagerState.currentPage == index) 20.dp else 8.dp, 8.dp).clip(CircleShape).background(if (pagerState.currentPage == index) AccentRed else TextSecondary))
                                    }
                                }
                            }
                        }
                    }

                    if (latestAnime.isNotEmpty()) {
                        item {
                            Column(modifier = Modifier.padding(top = 24.dp)) {
                                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Recent Releases (Live)", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text("View More", color = AccentRed, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    items(latestAnime) { anime ->
                                        // هنا نمرر دالة الضغط على الصورة (تذهب للتفاصيل) ودالة الضغط على النقاط الثلاث (تفتح الشيت)
                                        // كبديل مؤقت، سنستخدم الضغط العادي لفتح الشيت
                                        AnimeCard(
                                            title = anime.title,
                                            posterUrl = anime.posterUrl ?: "",
                                            episode = "Episode: ${anime.episodeNumber ?: "?"}",
                                            score = anime.score ?: "N/A",
                                            votes = "0",
                                            status = anime.status ?: "N/A",
                                            ageRating = "+17",
                                            onClick = { selectedAnimeForSheet = anime }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // عرض النافذة المنسدلة إذا تم اختيار أنمي
                selectedAnimeForSheet?.let { anime ->
                    AnimeBottomSheet(
                        anime = anime,
                        onDismiss = { selectedAnimeForSheet = null },
                        onNavigateToDetails = {
                            selectedAnimeForSheet = null
                            onAnimeClick(anime)
                        }
                    )
                }
            }
        }
    }
}
