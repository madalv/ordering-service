package com.madalv.plugins

import RestaurantData
import com.madalv.*
import io.ktor.client.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.coroutines.launch
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
            val takeoutlist: TakeoutList = call.receive()
            var responselist = TakeoutResponseList(takeoutlist.id, mutableListOf())

            if (!restaurants.any()) logger.debug { "NO RESTAURANTS YET, WHAT ARE YOU ORDERING?" }
            else for (takeout in takeoutlist.orders) {

                val order = Order(takeout.items, takeout.priority, takeout.maxWait, takeout.createdTime)

                // TODO figure wtf is going wrong
//                    val response: TakeoutResponse = client.post("http://${restaurants[takeout.restaurantID - 1].address}/v2/order") {
//                        contentType(ContentType.Application.Json)
//                        setBody(Json.encodeToJsonElement(order))
//                    }.body()

                client.post("http://${restaurants[takeout.restaurantID - 1].address}/v2/order") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToJsonElement(order))
                }

                // TODO add response to response list

                logger.debug { "SENT $order TO RESTAURANT ${restaurants[takeout.restaurantID - 1].name} @ ${restaurants[takeout.restaurantID - 1].address}" }
                logger.debug { Json.encodeToJsonElement(order) }
                //logger.debug { response }
            }

            // TODO call.response(responselist)
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
