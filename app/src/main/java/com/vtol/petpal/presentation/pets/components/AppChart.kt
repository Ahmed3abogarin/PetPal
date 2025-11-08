package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.toVicoShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.Shape
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppChart(modifier: Modifier = Modifier, records: List<WeightRecord>) {
    val sorted = records.sortedBy { it.date }
    val xValues = sorted.mapIndexed { index, _ -> index.toFloat() }
    val yValues = sorted.map { it.weight.toFloat() }

    val modelProducer = remember { CartesianChartModelProducer() }

    runBlocking {
        modelProducer.runTransaction {
            lineSeries {
                series(x = xValues, y = yValues)
            }
        }
    }
    val shape: Shape = CircleShape.toVicoShape()

    val startColor = Color(0xFF673AB7).copy(alpha = 0.5f) // Purple (Semi-transparent)
    val endColor = Color.Transparent


    // Here we define the line proprietaries (the art :) )
    val line = LineCartesianLayer.rememberLine(
        // This the line color
        fill = LineCartesianLayer.LineFill.single(
            fill = fill(MainPurple)
        ),
        // This is the area under the line (gradient effect)
        areaFill = LineCartesianLayer.AreaFill.single(
            fill = fill(
                ShaderProvider.verticalGradient(
                    // Converts Compose Colors to the IntArray Vico needs
                    colors = intArrayOf(
                        startColor.toArgb(),
                        endColor.toArgb()
                    )
                )
            )
        ),
        // This is the line width itself
        stroke = LineCartesianLayer.LineStroke.continuous(thickness = 3.dp),

        // The dot for each weight
        pointProvider = LineCartesianLayer.PointProvider.single(
            LineCartesianLayer.point(
                component = ShapeComponent(shape = shape, fill = fill(MainPurple)),
                size = 8.dp,
            )
        ),

//        pointConnector = LineCartesianLayer.PointConnector.rounded
    )

    // TODO: change the weight property in Pet to List of `WeightWithRecord` class, the weight will be provided by the user and the date will be automatically using firebase time instead of relying on the local time :)

    // Create the Cartesian chart
    val lineLayer =
        rememberLineCartesianLayer(lineProvider = LineCartesianLayer.LineProvider.series(line))

    CartesianChartHost(
        chart = rememberCartesianChart(
            lineLayer,
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = HorizontalAxis.ItemPlacer.aligned(),
                valueFormatter = { _, value, _ ->
                    val index = value.toInt()
                    if (index in sorted.indices) {
                        SimpleDateFormat("MMM d", Locale.getDefault())
                            .format(sorted[index].date)
                    } else ""
                }
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(16.dp)
    )
}


@Preview
@Composable
fun MyPreview2() {
    PetPalTheme {
        AppChart(
            records = listOf(
                WeightRecord(Date(), 5.0),
                WeightRecord(Date(), 6.0),
                WeightRecord(Date(), 1.4),
                WeightRecord(Date(), 1.4),
                WeightRecord(Date(), 2.0),
                WeightRecord(Date(), 1.4)
            )
        )

    }
}