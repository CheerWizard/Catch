package com.cws.acatch.game.networking.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val login: String
)