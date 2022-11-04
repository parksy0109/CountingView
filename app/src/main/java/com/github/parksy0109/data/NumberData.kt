package com.github.parksy0109.data

import java.text.DecimalFormat

data class NumberData(
    val thousands: Int,
    val hundreds: Int,
    val tens: Int,
    val units: Int,
)

fun Int.toNumberData(): NumberData {
    val decimalFormat = DecimalFormat("#,#")
    val format = decimalFormat.format(this)
    val split = format.split(",")

    return NumberData(
        if(split.size == 4) split[0].toInt() else 0,
        if (split.size >= 3) split[split.size -3].toInt() else 0,
        if (split.size >= 2) split[split.size - 2].toInt() else 0,
        if (split.size == 1) split[0].toInt() else split[split.size - 1].toInt(),
    )
}
