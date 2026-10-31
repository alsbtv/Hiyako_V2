package com.hiyako.app.data.scrapers

import com.hiyako.app.data.apis.AniListApi
import com.hiyako.app.data.apis.JikanApi
import com.hiyako.app.data.model.ScrapedAnime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers

object ScraperManager {

    // قائمة الكاشطات (حالياً Anime3rb، ونضيف البقية لاحقاً)
    private val scrapers: List<BaseScraper> = listOf(Anime3rbScraper())

    /**
     * جلب الأنمي المترند مع الدمج الذكي (Smart Merge) بين AniList و Jikan
     */
    suspend fun getTrendingSmartMerged(): List<ScrapedAnime> {
        // 1. جلب البيانات الأساسية من AniList
        val anilistData = AniListApi.getTrendingAnime()

        // 2. الدمج مع Jikan (لجلب القصة وتقييم MAL)
        return anilistData.map { anime ->
            if (anime.idMal != null) {
                val jikanData = JikanApi.getAnimeDetails(anime.idMal)
                if (jikanData != null) {
                    anime.copy(
                        synopsis = jikanData.synopsis ?: anime.synopsis,
                        malScore = jikanData.score
                    )
                } else anime
            } else anime
        }
    }

    /**
     * جلب الأنمي من الكاشطات (Scrapers) على دفعات (Batches) 
     * لتجنب الحظر (5 مواقع كل 30 ثانية)
     */
    fun getLatestScrapedAnimeFlow(): Flow<List<ScrapedAnime>> = flow {
        val batchSize = 5
        
        for (i in scrapers.indices step batchSize) {
            val currentBatch = scrapers.drop(i).take(batchSize)
            val batchResults = mutableListOf<ScrapedAnime>()
            
            for (scraper in currentBatch) {
                try {
                    val results = scraper.getLatestAnime()
                    batchResults.addAll(results)
                } catch (e: Exception) {
                    e.printStackTrace() // نتجاهل الموقع إذا فشل ونكمل البقية
                }
            }
            
            // إرسال النتائج للواجهة
            emit(batchResults)
            
            // إذا لم نكن في الدفعة الأخيرة، ننتظر 30 ثانية
            if (i + batchSize < scrapers.size) {
                delay(30000)
            }
        }
    }.flowOn(Dispatchers.IO)
}
