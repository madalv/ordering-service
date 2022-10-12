package com.madalv

import RestaurantData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Serializable
class MenusData(
    val restaurants: Int,
    @SerialName("restaurants_data") val restaurantsData: MutableList<RestaurantData>
)