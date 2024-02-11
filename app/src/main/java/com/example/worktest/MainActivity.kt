package com.example.worktest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentPage by remember { mutableIntStateOf(0) }
            var url by remember { mutableStateOf("") }

            when (currentPage) {
                0 -> Screen1 { newUrl ->
                    url = newUrl
                    currentPage = 1
                }
                1 -> Screen2(url = url)
            }
        }
    }
}

