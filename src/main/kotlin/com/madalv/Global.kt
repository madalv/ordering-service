package com.madalv

import RestaurantData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import java.io.File
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

val configJson = File("config/config.json").inputStream().readBytes().toString(Charsets.UTF_8)
val cfg: Config = Json.decodeFromString(Config.serializer(), configJson)

val restaurants: MutableList<RestaurantData> = CopyOnWriteArrayList()
val logger = KotlinLogging.logger {}
val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

val ratingChannel = Channel<Double>(Channel.UNLIMITED)
val nrOrders = AtomicInteger(0)
var avgRating: Double = 0.0

suspend fun calculateRating() {
    for (rating in ratingChannel) {
        avgRating += rating
        logger.debug { " ---------------------------------- AVG RATING IS ${avgRating / nrOrders.get()}" }
    }
}