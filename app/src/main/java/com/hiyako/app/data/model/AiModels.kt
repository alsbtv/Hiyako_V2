package com.hiyako.app.data.model

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val isLoading: Boolean = false,
    val spoilerLevel: String? = null // "None", "Mild", "Major"
)

val availableModels = listOf(
    "gpt-4o",
    "claude-3-5-sonnet",
    "google/gemini-1.5-pro",
    "meta-llama/llama-3-70b-instruct",
    "mistralai/mistral-large"
)
