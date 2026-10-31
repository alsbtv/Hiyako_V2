package com.hiyako.app.ui.screens

import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiyako.app.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(ChatMessage("Hello! I'm SORA AI. How can I help you with your anime journey today?", isUser = false))
    )
    val messages = _messages.asStateFlow()

    private val _selectedModel = MutableStateFlow("gpt-4o")
    val selectedModel = _selectedModel.asStateFlow()

    private var webView: WebView? = null
    private var isWebViewReady = false

    fun setWebView(wv: WebView) {
        this.webView = wv
    }

    fun setWebViewReady() {
        isWebViewReady = true
    }

    fun changeModel(model: String) {
        _selectedModel.value = model
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || !isWebViewReady) return

        // إضافة رسالة المستخدم
        _messages.value = _messages.value + ChatMessage(text, isUser = true)
        // إضافة رسالة تحميل
        _messages.value = _messages.value + ChatMessage("", isUser = false, isLoading = true)

        // إرسال الطلب لـ WebView (JavaScript)
        val escapedText = text.replace("\"", "\\\"").replace("\n", "\\n")
        val model = _selectedModel.value
        val js = "window.askPuter(\"$escapedText\", \"$model\");"
        
        viewModelScope.launch {
            webView?.evaluateJavascript(js, null)
        }
    }

    // دالة تُستدعى من JavaScript عند وصول الرد
    fun onAiResponse(response: String) {
        _messages.value = _messages.value.filter { !it.isLoading } + ChatMessage(response, isUser = false)
    }

    fun onAiError(error: String) {
        _messages.value = _messages.value.filter { !it.isLoading } + ChatMessage("Error: $error", isUser = false)
    }
}

// كلاس الجسر بين JS و Kotlin
class AiJsBridge(private val viewModel: AiViewModel) {
    @JavascriptInterface
    fun onResponse(response: String) {
        viewModel.onAiResponse(response)
    }

    @JavascriptInterface
    fun onError(error: String) {
        viewModel.onAiError(error)
    }

    @JavascriptInterface
    fun onReady() {
        viewModel.setWebViewReady()
    }
}
