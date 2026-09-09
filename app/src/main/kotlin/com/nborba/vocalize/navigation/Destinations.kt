package com.nborba.vocalize.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailRoute(
    val id: String,
)
