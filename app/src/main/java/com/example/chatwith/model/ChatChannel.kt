package com.example.chatwith.model

data class ChatChannel(val userIds: MutableList<String>)  {
    constructor(): this(mutableListOf())
}