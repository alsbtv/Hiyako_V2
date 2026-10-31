package com.hiyako.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.hiyako.app.data.model.ScrapedAnime
import com.hiyako.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeBottomSheet(
    anime: ScrapedAnime,
    onDismiss: () -> Unit,
    onNavigateToDetails: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CardSurface,
        contentColor = TextPrimary
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(anime.posterUrl).crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(100.dp).height(150.dp).clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToDetails() } // الضغط على الصورة ينقل للتفاصيل
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = anime.title,
                        fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Status: ${anime.status ?: "N/A"}", color = TextSecondary, fontSize = 14.sp)
                    Text("Episodes: ${anime.episodeNumber ?: "?"}", color = TextSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.background(MalBlue, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("MAL ${anime.malScore ?: "-"}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.background(AccentRed, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("Ani ${anime.score ?: "-"}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Story", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(anime.synopsis ?: "No synopsis available.", color = TextSecondary, fontSize = 14.sp, maxLines = 4, overflow = TextOverflow.Ellipsis)
            
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onNavigateToDetails() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Go to Details Page", fontWeight = FontWeight.Bold)
            }
        }
    }
}
