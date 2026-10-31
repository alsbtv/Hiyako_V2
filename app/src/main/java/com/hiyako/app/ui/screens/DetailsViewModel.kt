package com.hiyako.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiyako.app.data.apis.JikanApi
import com.hiyako.app.data.model.Episode
import com.hiyako.app.data.model.ScrapedAnime
import com.hiyako.app.data.scrapers.Anime3rbScraper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val _episodes = MutableStateFlow<List<Episode>>(emptyList())
    val episodes = _episodes.asStateFlow()

    private val _galleryImages = MutableStateFlow<List<String>>(emptyList())
    val galleryImages = _galleryImages.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun loadDetails(anime: ScrapedAnime) {
        viewModelScope.launch {
            _isLoading.value = true
            // جلب الحلقات من Anime3rb
            try {
                val eps = Anime3rbScraper().getEpisodes(anime.url)
                _episodes.value = eps
            } catch (e: Exception) { e.printStackTrace() }

            // جلب صور المعرض من Jikan
            if (anime.idMal != null) {
                try {
                    val images = JikanApi.getAnimePictures(anime.idMal)
                    _galleryImages.value = images
                } catch (e: Exception) { e.printStackTrace() }
            }
            
            _isLoading.value = false
        }
    }
}
