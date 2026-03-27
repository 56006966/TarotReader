package com.example.tarotreader.ui.background

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.Context
import android.util.AttributeSet
import android.util.LruCache
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.tarotreader.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class MysticBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val primaryImage = createImageView()
    private val secondaryImage = createImageView()
    private var imageResIds: List<Int> = emptyList()
    private var currentIndex = 0
    private var currentImageRes: Int? = null
    private val bitmapCache = LruCache<String, Bitmap>(6)
    private val decodeExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val requestVersion = AtomicInteger(0)

    init {
        clipChildren = true
        clipToPadding = true
        addView(primaryImage)
        addView(secondaryImage)
    }

    fun setImages(images: List<Int>) {
        imageResIds = images.distinct()
        currentIndex = 0
        if (imageResIds.isEmpty()) {
            primaryImage.setImageDrawable(null)
            secondaryImage.setImageDrawable(null)
            return
        }
        primaryImage.alpha = 1f
        primaryImage.scaleX = 1f
        primaryImage.scaleY = 1f
        setImage(primaryImage, imageResIds.first())
        secondaryImage.alpha = 0f
    }

    fun animateInStaticImage(imageRes: Int) {
        currentIndex = 0
        setImage(primaryImage, imageRes)
        primaryImage.alpha = 0f
        primaryImage.scaleX = 1.08f
        primaryImage.scaleY = 1.08f
        secondaryImage.alpha = 0f
        primaryImage.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(BACKGROUND_REVEAL_MS)
            .start()
    }

    fun showRandomImage() {
        if (imageResIds.isEmpty()) return
        val nextIndex = if (imageResIds.size == 1) {
            0
        } else {
            generateSequence { Random.nextInt(imageResIds.size) }
                .first { it != currentIndex }
        }
        setImage(secondaryImage, imageResIds[nextIndex])
        secondaryImage.alpha = 0f
        secondaryImage.scaleX = 1.02f
        secondaryImage.scaleY = 1.02f
        secondaryImage.visibility = View.VISIBLE
        secondaryImage.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(CROSSFADE_MS)
            .withEndAction {
                setImage(primaryImage, imageResIds[nextIndex])
                primaryImage.alpha = 1f
                primaryImage.scaleX = 1f
                primaryImage.scaleY = 1f
                secondaryImage.alpha = 0f
                currentIndex = nextIndex
            }
            .start()
    }

    fun showImage(imageRes: Int) {
        currentIndex = imageResIds.indexOf(imageRes).takeIf { it >= 0 } ?: 0
        setImage(primaryImage, imageRes)
        primaryImage.alpha = 1f
        primaryImage.scaleX = 1f
        primaryImage.scaleY = 1f
        secondaryImage.animate().cancel()
        secondaryImage.setImageDrawable(null)
        secondaryImage.alpha = 0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == oldw && h == oldh) return
        currentImageRes?.let { imageRes ->
            post {
                setImage(primaryImage, imageRes)
                secondaryImage.setImageBitmap(null)
                secondaryImage.alpha = 0f
            }
        }
    }

    override fun onDetachedFromWindow() {
        requestVersion.incrementAndGet()
        decodeExecutor.shutdownNow()
        super.onDetachedFromWindow()
    }

    private fun createImageView(): ImageView {
        return ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundColor(context.getColor(R.color.black))
        }
    }

    private fun setImage(imageView: ImageView, imageRes: Int) {
        val targetWidth = width.takeIf { it > 0 } ?: resources.displayMetrics.widthPixels
        val targetHeight = height.takeIf { it > 0 } ?: resources.displayMetrics.heightPixels
        val cacheKey = "$imageRes:$targetWidth:$targetHeight"
        currentImageRes = imageRes
        val cachedBitmap = bitmapCache.get(cacheKey)
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap)
            return
        }

        val version = requestVersion.incrementAndGet()
        decodeExecutor.execute {
            val bitmap = decodeSampledBitmap(imageRes, targetWidth, targetHeight) ?: return@execute
            bitmapCache.put(cacheKey, bitmap)
            post {
                if (!isAttachedToWindow || requestVersion.get() != version) return@post
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun decodeSampledBitmap(imageRes: Int, targetWidth: Int, targetHeight: Int): Bitmap? {
        val boundsOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, imageRes, boundsOptions)
        val sampleSize = calculateInSampleSize(
            sourceWidth = boundsOptions.outWidth,
            sourceHeight = boundsOptions.outHeight,
            targetWidth = targetWidth.coerceAtLeast(1),
            targetHeight = targetHeight.coerceAtLeast(1)
        )
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.RGB_565
        }
        return BitmapFactory.decodeResource(resources, imageRes, decodeOptions)
    }

    private fun calculateInSampleSize(
        sourceWidth: Int,
        sourceHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        var inSampleSize = 1
        if (sourceHeight <= 0 || sourceWidth <= 0) return inSampleSize

        var halfHeight = sourceHeight / 2
        var halfWidth = sourceWidth / 2
        while ((halfHeight / inSampleSize) >= targetHeight &&
            (halfWidth / inSampleSize) >= targetWidth
        ) {
            inSampleSize *= 2
        }
        return inSampleSize.coerceAtLeast(1)
    }

    companion object {
        private const val CROSSFADE_MS = 900L
        private const val BACKGROUND_REVEAL_MS = 900L
    }
}
