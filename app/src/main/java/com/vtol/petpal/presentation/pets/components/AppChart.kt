package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AppChart(modifier: Modifier = Modifier, records: List<WeightRecord>) {
    val sorted = remember(records) {
        records.sortedBy { it.timestamp }
    }

    val modelProducer = remember { CartesianChartModelProducer() }

    val shape: Shape = CircleShape.toVicoShape()

    val startColor = Color(0xFF673AB7).copy(alpha = 0.5f)
    val endColor = Color.Transparent

    // Convert data safely
    val xValues = remember(sorted) {
        sorted.mapIndexed { index, _ -> index.toFloat() }
    }

    val yValues = remember(sorted) {
        sorted.map { it.weight.toFloat() }
    }

    // Update chart safely
    LaunchedEffect(sorted) {
        if (sorted.isNotEmpty()) {
            modelProducer.runTransaction {
                lineSeries {
                    series(x = xValues, y = yValues)
                }
            }
        }
    }

    // EMPTY STATE HANDLING
    if (sorted.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = "No weight records yet")
        }
        return
    }

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
                            .format(sorted[index].timestamp)
                    } else {
                        " "
                    }
                }
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
//            .aspectRatio(1.7f)
    )
}


@Preview
@Composable
fun MyPreview2() {
    PetPalTheme {
        val testWeights = listOf(
            WeightRecord(weight = 3.2, unit = WeightUnit.KG, timestamp = 1700010800000),
        )
        AppChart(
            records = testWeights
        )

    }
}