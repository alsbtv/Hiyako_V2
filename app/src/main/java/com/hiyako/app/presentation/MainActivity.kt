package com.hiyako.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hiyako.app.presentation.character.CharacterDetailsScreen
import com.hiyako.app.presentation.details.DetailsScreen
import com.hiyako.app.presentation.home.HomeScreen
import com.hiyako.app.presentation.library.LibraryScreen
import com.hiyako.app.presentation.player.PlayerScreen
import com.hiyako.app.presentation.profile.ProfileScreen
import com.hiyako.app.presentation.search.FilterScreen
import com.hiyako.app.presentation.search.SearchScreen
import com.hiyako.app.presentation.settings.SettingsScreen
import com.hiyako.app.presentation.settings.SettingsViewModel
import com.hiyako.app.presentation.splash.SplashScreen
import com.hiyako.app.presentation.theme.HiyakoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themeColor by settingsViewModel.themeColor.collectAsState()

            HiyakoTheme(themeColor = themeColor) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBottomBar = currentRoute in listOf("home", "library", "profile")
                val showDrawer = currentRoute != "splash" && currentRoute != "player"

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = showDrawer,
                    drawerContent = {
                        ModalDrawerSheet(drawerContainerColor = Color(0xFF0A0A0A), modifier = Modifier.width(300.dp)) {
                            DrawerContent(navController, drawerState, scope)
                        }
                    }
                ) {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = { if (showBottomBar) CustomBottomNav(navController, currentRoute) }
                    ) { padding ->
                        NavHost(navController = navController, startDestination = "splash", modifier = Modifier.padding(padding)) {
                            composable("splash") { SplashScreen(navController) }
                            composable("home") { HomeScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
                            composable("search") { SearchScreen(navController) }
                            composable("filter") { FilterScreen(navController) }
                            composable("library") { LibraryScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
                            composable("profile") { ProfileScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
                            composable("settings") { SettingsScreen(navController) }
                            composable("details/{title}/{posterUrl}") { backStackEntry ->
                                val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
                                val posterUrl = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("posterUrl") ?: "", "UTF-8")
                                DetailsScreen(title, posterUrl, navController)
                            }
                            composable("character/{name}/{image}") { backStackEntry ->
                                val name = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", "UTF-8")
                                val image = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("image") ?: "", "UTF-8")
                                CharacterDetailsScreen(name, image, navController)
                            }
                            composable("player/{videoUrl}") { backStackEntry ->
                                val videoUrl = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("videoUrl") ?: "", "UTF-8")
                                PlayerScreen(videoUrl, navController)
                            }
                            val placeholders = listOf("recent", "dub", "trending", "schedule", "coming", "seasons", "history", "downloads", "mylist", "customlist", "notified", "characters", "favorites", "ourapps", "random", "competitions", "referral")
                            placeholders.forEach { route ->
                                composable(route) { PlaceholderScreen(route.replaceFirstChar { it.uppercase() }, onMenuClick = { scope.launch { drawerState.open() } }) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState, scope: kotlinx.coroutines.CoroutineScope) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))) {
            Text("Profile Header", color = Color.Gray, modifier = Modifier.align(Alignment.Center))
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        val items = listOf(
            DrawerItem("Home", Icons.Default.Home, "home"),
            DrawerItem("Search", Icons.Default.Search, "search"),
            DrawerItem("Pick Random Anime", Icons.Default.Refresh, "random"),
            DrawerItem("Competitions & Prizes", Icons.Default.EmojiEvents, "competitions"),
            DrawerItem("Referral & Rewards", Icons.Default.CardGiftcard, "referral"),
            DrawerItem("Recent Releases", Icons.Default.Whatshot, "recent"),
            DrawerItem("Recent Dub Releases", Icons.Default.Translate, "dub"),
            DrawerItem("Trending Anime's", Icons.Default.TrendingUp, "trending"),
            DrawerItem("Schedule", Icons.Default.DateRange, "schedule"),
            DrawerItem("Coming Soon", Icons.Default.NewReleases, "coming"),
            DrawerItem("Library", Icons.Default.LibraryBooks, "library"),
            DrawerItem("Seasons", Icons.Default.CalendarMonth, "seasons"),
            DrawerItem("Watch History", Icons.Default.History, "history"),
            DrawerItem("Downloads/Offline", Icons.Default.CloudDownload, "downloads"),
            DrawerItem("My List", Icons.Default.Checklist, "mylist"),
            DrawerItem("Custom Lists", Icons.Default.List, "customlist"),
            DrawerItem("Notified Anime", Icons.Default.NotificationsActive, "notified"),
            DrawerItem("Popular Characters", Icons.Default.Group, "characters"),
            DrawerItem("Favorite Characters", Icons.Default.Favorite, "favorites"),
            DrawerItem("Our Other Apps", Icons.Default.Apps, "ourapps"),
            DrawerItem("Settings", Icons.Default.Settings, "settings")
        )

        items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable {
                    scope.launch { drawerState.close() }
                    navController.navigate(item.route) { popUpTo("home") { inclusive = false } }
                }.padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(item.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(item.title, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

data class DrawerItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun CustomBottomNav(navController: NavController, currentRoute: String?) {
    NavigationBar(containerColor = Color(0xFF1E1E1E), tonalElevation = 0.dp) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = null) }, label = { Text("Home") }, selected = currentRoute == "home", onClick = { navController.navigate("home") }, colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray))
        NavigationBarItem(icon = { Icon(Icons.Default.List, contentDescription = null) }, label = { Text("Library") }, selected = currentRoute == "library", onClick = { navController.navigate("library") }, colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray))
        NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = null) }, label = { Text("Profile") }, selected = currentRoute == "profile", onClick = { navController.navigate("profile") }, colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray))
    }
}

@Composable
fun PlaceholderScreen(title: String, onMenuClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White) }
            Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Coming Soon: $title", color = Color.Gray, fontSize = 16.sp)
        }
    }
}
