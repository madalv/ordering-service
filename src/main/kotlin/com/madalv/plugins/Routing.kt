package com.madalv.plugins

import MenusData
import RestaurantData
import com.madalv.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*

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
            val responselist = TakeoutResponseList(1, mutableListOf())

            if (!restaurants.any()) logger.debug { "NO RESTAURANTS YET, WHAT ARE YOU ORDERING?" }
            else for (takeout in takeoutlist.orders) {

                val order = Order(takeout.items, takeout.priority, takeout.maxWait, takeout.createdTime)

                // run blocking is necessarry cause otherwise the call.respond() at the end has nothing to send back
                runBlocking {
                    val response: TakeoutResponse = client.post("http://${restaurants[takeout.restaurantID - 1].address}/v2/order") {
                        contentType(ContentType.Application.Json)
                        setBody(order)
                    }.body()
                    println(response)
                    responselist.responses.add(response)
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
        launch {
            val ratings: Ratings = call.receive()
            logger.debug { "GOT RATINGS $ratings" }
            var currRating = 0.0
            for (rating in ratings.ratings) {
                val response = async {
                    client.post("http://${restaurants[rating.restaurantId.minus(1)].address}/v2/rating") {
                        contentType(ContentType.Application.Json)
                        setBody(rating)
                    }
                }
                currRating += response.await().body<RestaurantResponse>().resAvgRating
                nrOrders.getAndIncrement()
                logger.debug { "$currRating $nrOrders" }
                ratingChannel.send(currRating / ratings.ratings.size)
                logger.debug { "${currRating/ ratings.ratings.size}  NRORDERS $nrOrders" }
                }
            }
        }
    }
}
