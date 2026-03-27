package com.example.tarotreader.ui.launch

import android.content.Context
import android.util.AttributeSet
import android.view.Choreographer
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.tarotreader.R
import com.example.tarotreader.utils.TarotDeck
import kotlin.math.roundToInt

class LaunchTarotTileWallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val viewport = FrameLayout(context)
    private val perspectiveStage = FrameLayout(context)
    private val scrollStrip = LinearLayout(context)
    private val choreographer by lazy { Choreographer.getInstance() }
    private var scrollFrameCallback: Choreographer.FrameCallback? = null

    private val tileGap = 14.dp()
    private val baseTileSize = 96.dp()
    private val imageResIds = TarotDeck.getAllCards().map { it.imageRes }
    private var lastFrameNanos = 0L
    private var segmentHeightPx = 0f
    private var scrollOffsetPx = 0f

    init {
        clipChildren = false
        clipToPadding = false

        viewport.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        viewport.clipChildren = true
        viewport.clipToPadding = true

        perspectiveStage.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        perspectiveStage.rotationX = 15f
        perspectiveStage.translationY = (-12).dp().toFloat()
        perspectiveStage.clipChildren = false
        perspectiveStage.clipToPadding = false

        scrollStrip.orientation = LinearLayout.VERTICAL
        scrollStrip.clipChildren = false
        scrollStrip.clipToPadding = false

        perspectiveStage.addView(scrollStrip)
        viewport.addView(perspectiveStage)
        addView(viewport)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rebuildWall()
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rebuildWall()
    }

    fun pauseAnimation() {
        stopAnimation()
    }

    fun resumeAnimation() {
        if (isShown) {
            if (segmentHeightPx > 0f && scrollStrip.childCount > 0) {
                startAnimation()
            } else {
                rebuildWall()
            }
        }
    }

    private fun rebuildWall() {
        if (!isAttachedToWindow || width <= 0 || height <= 0 || imageResIds.isEmpty()) return

        stopAnimation()
        scrollStrip.removeAllViews()

        val visibleRows = if (height >= 1600) 4 else 3
        val visibleCols = if (width >= 1200) 5 else 4
        val tileSize = (((width - (tileGap * (visibleCols - 1))) / visibleCols).coerceAtMost(baseTileSize * 2))
            .coerceAtLeast(baseTileSize)
        val rowCount = visibleRows * 4
        val segmentWidth = (visibleCols * tileSize) + ((visibleCols - 1) * tileGap)
        val segmentHeight = (rowCount * tileSize) + ((rowCount - 1) * tileGap)

        scrollStrip.layoutParams = LayoutParams(segmentWidth, segmentHeight * 3)
        perspectiveStage.layoutParams = LayoutParams(segmentWidth, height, Gravity.CENTER)

        repeat(3) { segmentIndex ->
            scrollStrip.addView(
                createSegment(
                    rowCount = rowCount,
                    cols = visibleCols,
                    tileSize = tileSize,
                    segmentWidth = segmentWidth,
                    segmentHeight = segmentHeight,
                    startIndexOffset = segmentIndex * visibleCols
                )
            )
        }

        scrollStrip.post {
            segmentHeightPx = segmentHeight.toFloat()
            scrollOffsetPx = 0f
            scrollStrip.translationY = -segmentHeightPx
            startAnimation()
        }
    }

    private fun createSegment(
        rowCount: Int,
        cols: Int,
        tileSize: Int,
        segmentWidth: Int,
        segmentHeight: Int,
        startIndexOffset: Int
    ): LinearLayout {
        val segment = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(segmentWidth, segmentHeight)
        }

        repeat(rowCount) { rowIndex ->
            val row = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    tileSize
                ).apply {
                    if (rowIndex < rowCount - 1) bottomMargin = tileGap
                }
            }

            repeat(cols) { colIndex ->
                val imageRes = imageResIds[(startIndexOffset + rowIndex * cols + colIndex) % imageResIds.size]
                row.addView(createTile(imageRes, tileSize, colIndex < cols - 1))
            }
            segment.addView(row)
        }

        return segment
    }

    private fun createTile(imageRes: Int, tileSize: Int, addEndGap: Boolean): View {
        val card = CardView(context).apply {
            radius = 18.dp().toFloat()
            cardElevation = 10.dp().toFloat()
            setCardBackgroundColor(context.getColor(R.color.tarot_card_surface))
            layoutParams = LinearLayout.LayoutParams(tileSize, (tileSize * 1.45f).roundToInt()).apply {
                if (addEndGap) rightMargin = tileGap
            }
        }

        val image = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setImageResource(imageRes)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setBackgroundColor(context.getColor(R.color.tarot_card_inner_surface))
            setPadding(8.dp(), 8.dp(), 8.dp(), 8.dp())
            alpha = 0.96f
        }

        card.addView(image)
        return card
    }

    private fun startAnimation() {
        if (scrollFrameCallback != null || segmentHeightPx <= 0f || !isShown) return
        lastFrameNanos = 0L
        scrollFrameCallback = Choreographer.FrameCallback { frameTimeNanos ->
            stepAnimation(frameTimeNanos)
        }
        choreographer.postFrameCallback(scrollFrameCallback)
    }

    private fun stepAnimation(frameTimeNanos: Long) {
        val callback = scrollFrameCallback ?: return
        if (!isShown || segmentHeightPx <= 0f) {
            stopAnimation()
            return
        }

        if (lastFrameNanos != 0L) {
            val deltaSeconds = (frameTimeNanos - lastFrameNanos) / 1_000_000_000f
            val pixelsPerSecond = 16f * resources.displayMetrics.density
            scrollOffsetPx = (scrollOffsetPx + (pixelsPerSecond * deltaSeconds)) % segmentHeightPx
            scrollStrip.translationY = -segmentHeightPx - scrollOffsetPx
        } else {
            scrollStrip.translationY = -segmentHeightPx
        }

        lastFrameNanos = frameTimeNanos
        choreographer.postFrameCallback(callback)
    }

    private fun stopAnimation() {
        scrollFrameCallback?.let(choreographer::removeFrameCallback)
        scrollFrameCallback = null
        lastFrameNanos = 0L
    }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).roundToInt()
}
