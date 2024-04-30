package com.example.unittestdemo.data


enum class TYPE {
    BOT, USER
}
data class TextModel(val text: String, val type: TYPE)
