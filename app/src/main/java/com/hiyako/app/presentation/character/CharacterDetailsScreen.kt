package com.hiyako.app.presentation.character

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun CharacterDetailsScreen(characterName: String, imageUrl: String, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115))) {
        // Blurred Background
        AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(400.dp).blur(30.dp).alpha(0.4f))
        Box(modifier = Modifier.fillMaxWidth().height(400.dp).background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF0F1115)))))

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // Top Bar
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }

            // Character Image
            Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.width(200.dp).height(300.dp).clip(RoundedCornerShape(16.dp)))
            }

            // Name and Actions
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(characterName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = { }) { Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White) }
                    IconButton(onClick = { }) { Icon(Icons.Default.Share, contentDescription = null, tint = Color.White) }
                }
            }

            // Details Section
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Details", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Age: 24", color = Color.LightGray, fontSize = 14.sp)
                Text("Birthday: 1866", color = Color.LightGray, fontSize = 14.sp)
                Text("Gender: Male", color = Color.LightGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Gyro has a cocky and flamboyant personality, appearing to have a high opinion of himself. However, he can be driven by emotions for other people and become realistic and serious when a problem presents itself.", color = Color.LightGray, fontSize = 14.sp, lineHeight = 22.sp)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
