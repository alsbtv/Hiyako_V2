package com.hiyako.app.data.apis

import com.hiyako.app.data.model.ScrapedAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

object JikanApi {

    private const val BASE_URL = "https://api.jikan.moe/v4"
    private const val WORKER_URL = "https://late-morning-434d.wesaamalsubhi.workers.dev/?url="
    private val client = OkHttpClient.Builder().build()

    suspend fun getAnimeDetails(malId: Int): ScrapedAnime? = withContext(Dispatchers.IO) {
        val targetUrl = "$BASE_URL/anime/$malId"
        val endpoint = WORKER_URL + URLEncoder.encode(targetUrl, "UTF-8")

        val request = Request.Builder().url(endpoint).build()

        try {
            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                if (!response.isSuccessful) throw IOException("Jikan HTTP Error: ${response.code}")

                val root = JSONObject(body)
                if (!root.has("data")) return@withContext null
                val data = root.getJSONObject("data")

                val title = data.optString("title").ifBlank { "Unknown" }
                val synopsis = data.optString("synopsis").takeIf { it.isNotBlank() && it != "null" }
                val score = data.optDouble("score", 0.0).takeIf { it > 0.0 }?.toString()

                ScrapedAnime(
                    title = title,
                    url = data.optString("url"),
                    posterUrl = null, // سنعتمد على AniList للبوستر
                    bannerUrl = null,
                    synopsis = synopsis,
                    score = score,
                    status = data.optString("status"),
                    sourceName = "Jikan"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAnimePictures(malId: Int): List<String> = withContext(Dispatchers.IO) {
        val targetUrl = "$BASE_URL/anime/$malId/pictures"
        val endpoint = WORKER_URL + URLEncoder.encode(targetUrl, "UTF-8")
        val request = Request.Builder().url(endpoint).build()
        val images = mutableListOf<String>()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string().orEmpty()
                    val root = JSONObject(body)
                    val dataArray = root.optJSONArray("data")
                    if (dataArray != null) {
                        for (i in 0 until dataArray.length()) {
                            val imgUrl = dataArray.getJSONObject(i).optJSONObject("jpg")?.optString("image_url")
                            if (imgUrl != null) images.add(imgUrl)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        images
    }
}
