package com.example.tarotreader.utils

import android.widget.ImageView
import com.example.tarotreader.R

object TarotCardFlipAnimator {

    fun bindCardFace(
        imageView: ImageView,
        isFaceUp: Boolean,
        frontRes: Int,
        isReversed: Boolean
    ) {
        val shownRes = if (isFaceUp) frontRes else R.drawable.card_back
        imageView.setImageResource(shownRes)
        imageView.tag = shownRes
        imageView.rotation = if (isFaceUp && isReversed) 180f else 0f
        imageView.rotationY = 0f
    }

    fun flip(
        imageView: ImageView,
        isFaceUp: Boolean,
        frontRes: Int,
        isReversed: Boolean,
        onFlipped: (() -> Unit)? = null
    ) {
        imageView.cameraDistance = imageView.resources.displayMetrics.density * 12000
        imageView.animate()
            .rotationY(90f)
            .setDuration(150)
            .withEndAction {
                bindCardFace(imageView, isFaceUp, frontRes, isReversed)
                imageView.rotationY = -90f
                imageView.animate()
                    .rotationY(0f)
                    .setDuration(150)
                    .withEndAction {
                        onFlipped?.invoke()
                    }
                    .start()
            }
            .start()
    }

    fun reveal(
        imageView: ImageView,
        frontRes: Int,
        isReversed: Boolean,
        onRevealed: (() -> Unit)? = null
    ) {
        imageView.cameraDistance = imageView.resources.displayMetrics.density * 12000
        imageView.animate()
            .rotationY(90f)
            .setDuration(140)
            .withEndAction {
                imageView.setImageResource(frontRes)
                imageView.tag = frontRes
                imageView.rotation = 0f
                imageView.rotationY = -90f
                imageView.animate()
                    .rotationY(0f)
                    .setDuration(140)
                    .withEndAction {
                        if (isReversed) {
                            imageView.animate()
                                .rotation(180f)
                                .setDuration(220)
                                .start()
                        }
                        onRevealed?.invoke()
                    }
                    .start()
            }
            .start()
    }
}
