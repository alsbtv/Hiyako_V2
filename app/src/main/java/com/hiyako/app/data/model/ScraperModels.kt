package com.hiyako.app.data.model

data class ScrapedAnime(
    val id: Int = 0, // معرف AniList
    val idMal: Int? = null, // معرف MyAnimeList
    val title: String,
    val url: String,
    val posterUrl: String? = null,
    val episodeNumber: Int? = null,
    val bannerUrl: String? = null,
    val synopsis: String? = null,
    val score: String? = null, // تقييم AniList
    val malScore: String? = null, // تقييم MAL
    val status: String? = null,
    val sourceName: String? = null
)

data class Episode(
    val number: Int,
    val title: String? = null,
    val thumbnailUrl: String? = null,
    val url: String
)

data class NewsArticle(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val source: String? = null,
    val publishedAt: String? = null,
    val translatedTitle: String? = null,
    val translatedDescription: String? = null,
    val relatedAnimeTitle: String? = null
)
