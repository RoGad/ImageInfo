package com.example.worktest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@Composable
fun Screen2(url: String = "") {
    var images by remember { mutableStateOf<List<ImageData>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = url) {
        if (url.isNotEmpty()) {
            loading = true // Устанавливаем флаг загрузки в true
            val doc = withContext(Dispatchers.IO) { Jsoup.connect(url).get() }
            val imgElements = doc.select("img")

            if (imgElements.isNotEmpty()) {
                images = imgElements.map { element ->
                    val imgUrl = element.absUrl("src")
                    val imgSize = element.attr("size")
                    val imgResolution = "${element.attr("width")}x${element.attr("height")}"
                    ImageData(imgUrl, imgSize, imgResolution)
                }
            }

            loading = false // Сбрасываем флаг загрузки после завершения загрузки изображений
        }
    }

    // Если идет загрузка, отобразим индикатор загрузки
    if (loading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(images) { imageData ->
                ImageListItem(imageData)
            }
        }
    }
}

data class ImageData(val url: String, val size: String, val resolution: String)

@Composable
fun ImageListItem(imageData: ImageData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        val painter: Painter = rememberAsyncImagePainter(model = imageData.url)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = "Изображение", fontWeight = FontWeight.Bold)
            Text(text = "Урл: ${imageData.url}")
            Text(text = "Размер в кб: ${imageData.size}")
            Text(text = "Разрешение: ${imageData.resolution}")
        }
    }
}