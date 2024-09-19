package kr.co.are.androidble.ui.component.chart

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.AutoScrollCondition
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Animation
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("RestrictedApi")
@Composable
fun GlucoseLineChart(
    isRealTime: Boolean,
    glucoseDataList: MutableList<GlucoseInfoEntity?>,
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier
) {
    val marker = rememberMarker()
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        remember { LineCartesianLayer.LineFill.single(fill(Color(0xffa485e0))) }
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                valueFormatter = { _, x, _ ->
                    try {
                        val utcTime =
                            glucoseDataList[x.toInt() % glucoseDataList.size]?.createdTime ?: ""
                        if (isRealTime) {
                            utcTime
                        } else {
                            if (utcTime.isNotEmpty()) {
                                val utcLocalDateTime = LocalDateTime.parse(
                                    utcTime,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                                )

                                val localZonedDateTime = utcLocalDateTime.atZone(ZoneId.of("UTC"))
                                    .withZoneSameInstant(ZoneId.systemDefault())

                                val formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")
                                val formattedDate = localZonedDateTime.format(formatter)
                                formattedDate
                            } else {
                                " "
                            }
                        }

                    } catch (e: Exception) {
                        " "
                    }
                },
                itemPlacer =
                remember {
                    HorizontalAxis.ItemPlacer.aligned(spacing = 1, addExtremeLabelPadding = true)
                },
            ),
            marker = marker,
            layerPadding = cartesianLayerPadding(
                scalableStartPadding = 0.dp,
                scalableEndPadding = 0.dp
            ),
            persistentMarkers = if (isRealTime) {
                rememberExtraLambda(marker) { marker at glucoseDataList.lastIndex }
            } else {
                null
            },
        ),
        modelProducer = modelProducer,
        modifier = modifier,
        zoomState = rememberVicoZoomState(zoomEnabled = true),
        scrollState = if (isRealTime) {
            rememberVicoScrollState(
                scrollEnabled = false,
                initialScroll = Scroll.Absolute.End,
                autoScrollCondition = AutoScrollCondition.OnModelSizeIncreased
            )
        } else {
            rememberVicoScrollState(
                scrollEnabled = true
            )
        },

        animationSpec = if (isRealTime) {
            null
        } else {
            tween(durationMillis = Animation.DIFF_DURATION)
        }
    )
}
