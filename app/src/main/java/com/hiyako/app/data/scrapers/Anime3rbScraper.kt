package com.hiyako.app.data.scrapers

import com.hiyako.app.data.model.Episode
import com.hiyako.app.data.model.ScrapedAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.net.URLEncoder

class Anime3rbScraper : BaseScraper {
    override val name: String = "Anime3rb"
    private val baseUrl = "https://anime3rb.com/"
    private val workerUrl = "https://late-morning-434d.wesaamalsubhi.workers.dev/?url="
    private val client = OkHttpClient.Builder().build()

    override suspend fun getLatestAnime(): List<ScrapedAnime> = withContext(Dispatchers.IO) {
        // (نفس الكود السابق لجلب أحدث الأنمي)
        val result = mutableListOf<ScrapedAnime>()
        val endpoint = workerUrl + URLEncoder.encode(baseUrl, "UTF-8")
        val request = Request.Builder().url(endpoint).header("User-Agent", "Mozilla/5.0").build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val doc = Jsoup.parse(response.body?.string().orEmpty())
                val cards = doc.select("div.anime-card, div.card, a[href*=/anime/]")
                for (card in cards) {
                    val title = card.selectFirst("h3, .title")?.text() ?: continue
                    val url = card.attr("href").let { if (it.startsWith("http")) it else baseUrl + it.removePrefix("/") }
                    val poster = card.selectFirst("img")?.attr("src")
                    result.add(ScrapedAnime(title = title, url = url, posterUrl = poster, sourceName = name))
                }
            }
        } catch (e: Exception) { e.printStackTrace() }
        result.distinctBy { it.url }
    }

    override suspend fun getEpisodes(animeUrl: String): List<Episode> = withContext(Dispatchers.IO) {
        val episodes = mutableListOf<Episode>()
        val endpoint = workerUrl + URLEncoder.encode(animeUrl, "UTF-8")
        val request = Request.Builder().url(endpoint).header("User-Agent", "Mozilla/5.0").build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val doc = Jsoup.parse(response.body?.string().orEmpty())
                // البحث عن روابط الحلقات
                val epLinks = doc.select("a[href*=/episode/], a[href*=/watch/]")
                for (link in epLinks) {
                    val epUrl = link.attr("href").let { if (it.startsWith("http")) it else baseUrl + it.removePrefix("/") }
                    val epTitle = link.text().trim()
                    // استخراج رقم الحلقة بـ Regex
                    val epNumber = Regex("""(\d+)""").find(epTitle)?.value?.toIntOrNull() ?: 0
                    if (epUrl.isNotBlank()) {
                        episodes.add(Episode(number = epNumber, title = epTitle, url = epUrl))
                    }
                }
            }
        } catch (e: Exception) { e.printStackTrace() }
        episodes.sortedBy { it.number }
    }

    override suspend fun getVideoUrl(episodeUrl: String): String? = withContext(Dispatchers.IO) {
        val endpoint = workerUrl + URLEncoder.encode(episodeUrl, "UTF-8")
        val request = Request.Builder().url(endpoint).header("User-Agent", "Mozilla/5.0").build()
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val doc = Jsoup.parse(response.body?.string().orEmpty())
                doc.selectFirst("iframe")?.attr("src") ?: doc.selectFirst("video source")?.attr("src")
            }
        } catch (e: Exception) { null }
    }
}
