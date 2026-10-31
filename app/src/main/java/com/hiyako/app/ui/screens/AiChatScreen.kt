package com.hiyako.app.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hiyako.app.data.model.availableModels
import com.hiyako.app.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    onBack: () -> Unit,
    viewModel: AiViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var showModelMenu by remember { mutableStateOf(false) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        // TopBar
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Text("✕", color = TextPrimary, fontSize = 24.sp) }
            Text("SORA AI", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
        }

        // Chat Area
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .background(if (msg.isUser) AccentRed else CardSurface, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        if (msg.isLoading) {
                            Text("Thinking...", color = TextSecondary, fontSize = 14.sp)
                        } else {
                            Text(msg.text, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Hidden WebView for Puter.js
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(AiJsBridge(viewModel), "AndroidBridge")
                    
                    val html = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <script src="https://js.puter.com/v2/"></script>
                        </head>
                        <body>
                            <script>
                                // ننتظر تحميل السكربت
                                window.onload = function() {
                                    AndroidBridge.onReady();
                                }
                                
                                window.askPuter = function(prompt, model) {
                                    puter.ai.chat(prompt, { model: model })
                                        .then(response => {
                                            AndroidBridge.onResponse(response.toString());
                                        })
                                        .catch(error => {
                                            AndroidBridge.onError(error.toString());
                                        });
                                }
                            </script>
                        </body>
                        </html>
                    """.trimIndent()
                    
                    loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                    webViewInstance = this
                    viewModel.setWebView(this)
                }
            },
            modifier = Modifier.size(0.dp) // إخفاء الـ WebView تماماً
        )

        // Bottom Input Area
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Model Selector
            Box {
                Row(
                    modifier = Modifier
                        .background(CardSurface, RoundedCornerShape(8.dp))
                        .clickable { showModelMenu = true }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedModel, color = TextPrimary, fontSize = 12.sp)
                    Text(" ▼", color = TextSecondary, fontSize = 10.sp)
                }
                
                DropdownMenu(
                    expanded = showModelMenu,
                    onDismissRequest = { showModelMenu = false },
                    modifier = Modifier.background(CardSurface)
                ) {
                    availableModels.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(model, color = TextPrimary) },
                            onClick = {
                                viewModel.changeModel(model)
                                showModelMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Text Field
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f).height(50.dp),
                placeholder = { Text("Ask anything...", color = TextSecondary, fontSize = 14.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedBorderColor = AccentRed,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send Button
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(if (inputText.isNotBlank()) AccentRed else Color.DarkGray, RoundedCornerShape(8.dp))
                    .clickable {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("➤", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
