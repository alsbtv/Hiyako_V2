package com.hiyako.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiyako.app.data.model.ScrapedAnime
import com.hiyako.app.data.scrapers.ScraperManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    
    private val _trendingAnime = MutableStateFlow<List<ScrapedAnime>>(emptyList())
    val trendingAnime = _trendingAnime.asStateFlow()

    private val _latestAnime = MutableStateFlow<List<ScrapedAnime>>(emptyList())
    val latestAnime = _latestAnime.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadRealData()
    }

    private fun loadRealData() {
        viewModelScope.launch {
            // 1. جلب بيانات الترند (AniList + Jikan) للبنر
            try {
                val trending = ScraperManager.getTrendingSmartMerged()
                _trendingAnime.value = trending
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }

        viewModelScope.launch {
            // 2. جلب أحدث الإصدارات (من كاشط Anime3rb)
            try {
                ScraperManager.getLatestScrapedAnimeFlow().collect { batch ->
                    // نضيف كل دفعة تصل إلى القائمة
                    _latestAnime.value = _latestAnime.value + batch
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
