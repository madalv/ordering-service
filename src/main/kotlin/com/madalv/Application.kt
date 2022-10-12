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


//        val o1 = TakeoutOrder(1, listOf(1, 2), 10, 50.5, System.currentTimeMillis())
//        val o2 = TakeoutOrder(1, listOf(1, 3), 10, 50.5, System.currentTimeMillis())
//        val takeoutlist = TakeoutList(1, 1, listOf(o1, o2))

//            launch {
//            while (true) {
//                if (!restaurants.any()) continue
//
//                logger.debug { restaurants.size }
//                delay(4000)
//
//                for (takeout in takeoutlist.orders) {
//
//                    val order = Order(takeout.items, takeout.priority, takeout.maxWait, takeout.createdTime)
//
////                    val response: TakeoutResponse = client.post("http://${restaurants[takeout.restaurantID - 1].address}/v2/order") {
////                        contentType(ContentType.Application.Json)
////                        setBody(Json.encodeToJsonElement(order))
////                    }.body()
//
//                    client.post("http://${restaurants[takeout.restaurantID - 1].address}/v2/order") {
//                        contentType(ContentType.Application.Json)
//                        setBody(Json.encodeToJsonElement(order))
//                    }
//
//                    // TODO add response to response list, send back to client
//
//                    logger.debug { "SENT $order TO RESTAURANT ${restaurants[takeout.restaurantID - 1].name} @ ${restaurants[takeout.restaurantID - 1].address}" }
//                    //logger.debug { response }
//                }
//            }
//        }
    }.start(wait = true)
}
