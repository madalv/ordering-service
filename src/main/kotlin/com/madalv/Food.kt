package com.madalv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class Food(
    val id: Int,
    val name: String,
    @SerialName("preparation-time") val preparationTime: Long,
    val complexity: Int,
    @SerialName("cooking-apparatus") val cookingApparatus: String? = null
) {
    override fun toString(): String {
        return "{$id: P:${preparationTime} CX:$complexity A:$cookingApparatus}"
    }
}

