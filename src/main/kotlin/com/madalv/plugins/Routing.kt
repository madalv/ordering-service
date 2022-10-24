package com.madalv.plugins

import RestaurantData
import com.madalv.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello Food Ordering Service!")
        }
        // CLIENT SERVICES GETS RESTAURANTS MENU
        get("/menu") {
            call.respond(MenusData(restaurants.size, restaurants))
        }
        // CLIENT SENDS ORDER
        post("/order") {
            launch {
                val takeoutlist: TakeoutList = call.receive()
                val responselist = TakeoutResponseList(takeoutlist.id, mutableListOf())

                if (!restaurants.any()) logger.debug { "NO RESTAURANTS YET, WHAT ARE YOU ORDERING?" }
                else for (takeout in takeoutlist.orders) {

                    val order = Order(takeout.items, takeout.priority, takeout.maxWait, takeout.createdTime)

                    // fuck this shit man
                    // run blocking is necessarry cause otherwise the call.respond() at the end has nothing to send back
                    runBlocking {
                        val response = client.post("http://${restaurants[takeout.restaurantID - 1].address}/v2/order") {
                            contentType(ContentType.Application.Json)
                            setBody(order)
                        }
                        //println(response)
                        responselist.orders.add(response.body())
                    }
                    logger.debug { "SENT $order TO RESTAURANT ${restaurants[takeout.restaurantID - 1].name} @ ${restaurants[takeout.restaurantID - 1].address}" }
                }

                call.respond(responselist)
            }
        }
        // RESTAURANT REGISTERS TO FOOD ORDERDING SERVICE
        post("/register") {
                val restaurant: RestaurantData = call.receive()
                logger.debug { "RESTAURANT REGISTERED: $restaurant" }
                restaurants.add(restaurant)
        }
        post("/rating") {

        }
    }
}
