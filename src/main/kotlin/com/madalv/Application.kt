  package com.madalv

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.madalv.plugins.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

fun main() {
    embeddedServer(Netty, port = cfg.port, host = "0.0.0.0") {
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
