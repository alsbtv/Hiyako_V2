package com.hiyako.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.StarOutline
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
import com.hiyako.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedNavItem by remember { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false, // تعطيل السحب لفتح الدراور
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DrawerBg,
                modifier = Modifier.width(300.dp)
            ) {
                // 1. رأس الدراور
                Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?auto=format&fit=crop&w=600&q=80",
                        contentDescription = "Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .clickable { scope.launch { drawerState.close() } },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✕", color = Color.White, fontSize = 16.sp)
                    }
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 16.dp)
                    ) {
                        AsyncImage(
                            model = "https://s4.anilist.co/file/anilistcdn/character/large/b1-1y1y1.jpg",
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                                .background(Color(0xFFFF9800), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
                    Text("alsb_tv", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("@alsb_tv", color = TextSecondary, fontSize = 14.sp)
                }

                val drawerItems = listOf(
                    "Home" to Icons.Filled.Home,
                    "Search" to Icons.Filled.Search,
                    "Pick Random Anime" to Icons.Outlined.List,
                    "Competitions & Prizes" to Icons.Outlined.StarOutline,
                    "Referral & Rewards" to Icons.Outlined.FavoriteBorder,
                    "Recent Releases" to Icons.Outlined.PlayCircleOutline,
                    "Recent Dub Releases" to Icons.Outlined.PlayCircleOutline,
                    "Trending Anime's" to Icons.Outlined.List,
                    "Schedule" to Icons.Outlined.DateRange,
                    "Coming Soon" to Icons.Outlined.DateRange,
                    "Library" to Icons.Outlined.BookmarkBorder,
                    "Seasons" to Icons.Outlined.DateRange,
                    "Watch History" to Icons.Outlined.PlayCircleOutline,
                    "Downloads/Offline" to Icons.Outlined.Download,
                    "My List" to Icons.Outlined.List,
                    "Custom Lists" to Icons.Outlined.List,
                    "Notified Anime" to Icons.Filled.Notifications,
                    "Popular Characters" to Icons.Outlined.List,
                    "Favorite Characters" to Icons.Outlined.FavoriteBorder,
                    "Our Other Apps" to Icons.Outlined.List,
                    "Settings" to Icons.Filled.Settings
                )
                
                LazyColumn {
                    items(drawerItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = item.second, contentDescription = item.first, tint = TextPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(item.first, color = TextPrimary, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = DarkBg,
            topBar = {
                // الشريط العلوي (تم تقليل الحشوة العلوية من 40 إلى 24 ليرتفع للأعلى)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = TextSecondary)
                    }
                    Text(
                        "Browse Anime", 
                        color = TextSecondary, 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold, 
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Alerts", tint = TextSecondary)
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextSecondary)
                        }
                    }
                }
            },
            bottomBar = {
                // شريط التنقل السفلي (4 أزرار)
                NavigationBar(containerColor = DrawerBg, modifier = Modifier.height(64.dp)) {
                    val navItems = listOf("Home", "Library", "News", "Profile")
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedNavItem == index,
                            onClick = { selectedNavItem = index },
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Home, contentDescription = "Home", tint = if (selectedNavItem == 0) AccentRed else TextSecondary)
                                    1 -> Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Library", tint = if (selectedNavItem == 1) AccentRed else TextSecondary)
                                    2 -> Icon(Icons.Outlined.Article, contentDescription = "News", tint = if (selectedNavItem == 2) AccentRed else TextSecondary)
                                    3 -> AsyncImage(
                                        model = "https://s4.anilist.co/file/anilistcdn/character/large/b1-1y1y1.jpg",
                                        contentDescription = "Profile",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(24.dp).clip(CircleShape)
                                    )
                                }
                            },
                            label = { Text(item, color = if (selectedNavItem == index) AccentRed else TextSecondary, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Content goes here...", color = TextSecondary)
            }
        }
    }
}
