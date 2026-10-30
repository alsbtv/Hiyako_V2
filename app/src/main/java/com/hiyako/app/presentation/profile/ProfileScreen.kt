package com.hiyako.app.presentation.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onMenuClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White) }
            Text("Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { }) { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White) }
        }

        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            AsyncImage(
                model = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/116674-ZXyP8fK5Jz8Z.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color(0x66000000)))
            
            AsyncImage(
                model = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx116674-2P4wLh56yH2W.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 16.dp, y = 50.dp)
                    .clip(CircleShape)
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 60.dp)) {
            Text("Alsbtv", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("@alsbtv", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Joined 4 Month (Jun 20 2026 - 10:31)", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1322", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Episodes", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("0", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Favorite Characters", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ACTIVITY", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(color = Color(0xFF00BCD4), startAngle = 0f, sweepAngle = 200f, useCenter = false, style = Stroke(width = 30f))
                            drawArc(color = Color(0xFFE50046), startAngle = 200f, sweepAngle = 100f, useCenter = false, style = Stroke(width = 30f))
                            drawArc(color = Color(0xFF4CAF50), startAngle = 300f, sweepAngle = 60f, useCenter = false, style = Stroke(width = 30f))
                        }
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ActivityRow(Icons.Default.Favorite, "Favorite", "6", Color(0xFFE50046))
                        ActivityRow(Icons.Default.PlayArrow, "Watching", "1", Color(0xFF00BCD4))
                        ActivityRow(Icons.Default.CheckCircle, "Completed", "2", Color(0xFF4CAF50))
                        ActivityRow(Icons.Default.Bookmark, "Plan To Watch", "48", Color(0xFF9C27B0))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ActivityRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, count: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.LightGray, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(count, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
