package com.hiyako.app.data.apis

import com.hiyako.app.data.model.ScrapedAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

object AniListApi {

    private const val ANILIST_URL = "https://graphql.anilist.co"
    private const val WORKER_URL = "https://late-morning-434d.wesaamalsubhi.workers.dev/?url="
    private val client = OkHttpClient.Builder().build()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    private val endpoint: String = WORKER_URL + URLEncoder.encode(ANILIST_URL, "UTF-8")

    private const val ANIME_FIELDS = """
        fragment AnimeFields on Media {
          id
          idMal
          title { romaji english native }
          description(asHtml: false)
          coverImage { extraLarge large }
          bannerImage
          averageScore
          episodes
          status
          siteUrl
        }
    """

    suspend fun getTrendingAnime(page: Int = 1, perPage: Int = 20): List<ScrapedAnime> = withContext(Dispatchers.IO) {
        val gql = """
            query (${'$'}page: Int, ${'$'}perPage: Int) {
              Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                media(type: ANIME, sort: TRENDING_DESC) {
                  ...AnimeFields
                }
              }
            }
            $ANIME_FIELDS
        """.trimIndent()

        val variables = JSONObject().put("page", page).put("perPage", perPage)
        parseMediaList(executeGraphQL(gql, variables))
    }

    suspend fun searchAnime(query: String, page: Int = 1, perPage: Int = 20): List<ScrapedAnime> = withContext(Dispatchers.IO) {
        val gql = """
            query (${'$'}search: String, ${'$'}page: Int, ${'$'}perPage: Int) {
              Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                media(search: ${'$'}search, type: ANIME, sort: POPULARITY_DESC) {
                  ...AnimeFields
                }
              }
            }
            $ANIME_FIELDS
        """.trimIndent()

        val variables = JSONObject().put("search", query).put("page", page).put("perPage", perPage)
        parseMediaList(executeGraphQL(gql, variables))
    }

    private fun parseMediaList(data: JSONObject): List<ScrapedAnime> {
        val mediaArray = data.optJSONObject("Page")?.optJSONArray("media") ?: return emptyList()
        val resultList = mutableListOf<ScrapedAnime>()
        for (i in 0 until mediaArray.length()) {
            mediaArray.optJSONObject(i)?.let { resultList.add(parseAnime(it)) }
        }
        return resultList
    }

    private fun executeGraphQL(query: String, variables: JSONObject): JSONObject {
        val payload = JSONObject().put("query", query).put("variables", variables).toString()
        val request = Request.Builder().url(endpoint).post(payload.toRequestBody(jsonMediaType)).header("Accept", "application/json").build()

        return client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw IOException("AniList HTTP Error ${response.code}: $body")
            val root = JSONObject(body)
            if (root.has("errors")) throw IOException("AniList GraphQL Error: ${root.getJSONArray("errors")}")
            root.getJSONObject("data")
        }
    }

    private fun parseAnime(json: JSONObject): ScrapedAnime {
        val titleObj = json.optJSONObject("title")
        val cover = json.optJSONObject("coverImage")
        val displayTitle = titleObj?.optString("romaji")?.takeIf { it.isNotBlank() } ?: "Unknown"

        return ScrapedAnime(
            id = json.optInt("id"),
            idMal = json.optInt("idMal").takeIf { it > 0 },
            title = displayTitle,
            url = json.optString("siteUrl").ifBlank { "https://anilist.co/anime/${json.optInt("id")}" },
            posterUrl = cover?.optString("extraLarge")?.takeIf { it.isNotBlank() } ?: cover?.optString("large"),
            episodeNumber = json.optInt("episodes").takeIf { it > 0 },
            bannerUrl = json.optString("bannerImage").takeIf { it.isNotBlank() },
            synopsis = json.optString("description").takeIf { it.isNotBlank() },
            score = json.optInt("averageScore").takeIf { it > 0 }?.let { "$it%" },
            status = json.optString("status").takeIf { it.isNotBlank() },
            sourceName = "AniList"
        )
    }
}
