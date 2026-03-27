package com.example.tarotreader

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tarotreader.databinding.ActivityLaunchBinding
import com.example.tarotreader.ui.background.MysticBackgroundImages

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private var isLaunchingHome = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.launchBackgroundView.animateInStaticImage(MysticBackgroundImages.launchImage)

        binding.launchEnterButton.setOnClickListener {
            if (!isLaunchingHome) {
                runLaunchCurtainAnimation()
            }
        }

        prepareLaunchScreen()
        runOpeningSequence()
    }

    private fun prepareLaunchScreen() {
        binding.launchRoot.alpha = 1f
        binding.launchContent.alpha = 0f
        binding.launchEnterButton.isEnabled = false
    }

    private fun runOpeningSequence() {
        binding.launchRoot.postDelayed({
            binding.launchContent.animate().alpha(1f).setDuration(360L).withEndAction {
                binding.launchEnterButton.isEnabled = true
            }.start()
        }, 240L)
    }

    private fun runLaunchCurtainAnimation() {
        isLaunchingHome = true
        binding.launchEnterButton.isEnabled = false
        binding.launchContent.animate()
            .alpha(0f)
            .setDuration(180L)
            .withEndAction {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
            .start()
    }
}
