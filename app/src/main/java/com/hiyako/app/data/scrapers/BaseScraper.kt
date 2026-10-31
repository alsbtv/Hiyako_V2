package com.hiyako.app.data.scrapers

import com.hiyako.app.data.model.Episode
import com.hiyako.app.data.model.ScrapedAnime

interface BaseScraper {
    val name: String
    suspend fun getLatestAnime(): List<ScrapedAnime>
    suspend fun getEpisodes(animeUrl: String): List<Episode> // دالة جديدة
    suspend fun getVideoUrl(episodeUrl: String): String?
}
