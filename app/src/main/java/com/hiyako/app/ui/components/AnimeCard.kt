package com.hiyako.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hiyako.app.ui.theme.*

@Composable
fun AnimeCard(
    title: String,
    posterUrl: String,
    episode: String,
    score: String,
    votes: String,
    status: String,
    ageRating: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.width(130.dp).clickable { onClick() }) {
        Box(modifier = Modifier.width(130.dp).height(180.dp).clip(RoundedCornerShape(8.dp))) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(posterUrl).crossfade(true).build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // شارة الحالة
            Box(
                modifier = Modifier.align(Alignment.TopStart).padding(4.dp)
                    .background(if (status == "On Going" || status == "RELEASING") OnGoingGreen else AccentRed, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(if (status == "RELEASING") "On Going" else "Completed", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            // شارة العمر
            Box(
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                    .background(AccentRed, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(ageRating, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            // شارة الحلقة
            Box(
                modifier = Modifier.align(Alignment.BottomStart).padding(4.dp)
                    .background(TransparentBlack, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(episode, color = Color.White, fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⭐", fontSize = 10.sp)
            Text(" $score ($votes Vote)", color = TextSecondary, fontSize = 10.sp)
        }
    }
}
