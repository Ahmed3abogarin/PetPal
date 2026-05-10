package com.vtol.petpal.util

import com.vtol.petpal.domain.model.WeightUnit
import kotlin.math.roundToInt

object WeightConverter {

    fun toGrams(
        value: Float,
        unit: WeightUnit
    ): Int {

        return when (unit) {

            WeightUnit.KG ->
                (value * 1000).roundToInt()

            WeightUnit.LBS ->
                (value * 453.592f).roundToInt()

            WeightUnit.G ->
                value.roundToInt()
        }
    }

    fun fromGrams(
        grams: Int,
        unit: WeightUnit
    ): Float {

        return when (unit) {

            WeightUnit.KG ->
                grams / 1000f

            WeightUnit.LBS ->
                grams / 453.592f

            WeightUnit.G ->
                grams.toFloat()
        }
    }
}