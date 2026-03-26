package com.example.tarotreader

import android.os.Bundle
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import com.example.tarotreader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var introDismissed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        introDismissed = savedInstanceState?.getBoolean(KEY_INTRO_DISMISSED) ?: false

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
        val navController = navHostFragment.navController

        val topLevelDestinations = setOf(R.id.nav_tarot, R.id.nav_horoscope)
        val navView: NavigationView? = findViewById(R.id.nav_view)
        appBarConfiguration = if (navView != null) {
            AppBarConfiguration(topLevelDestinations, binding.drawerLayout)
        } else {
            AppBarConfiguration(topLevelDestinations)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)
        binding.appBarMain.contentMain.bottomNavView?.setupWithNavController(navController)

        if (introDismissed) {
            hideLaunchOverlay()
        } else {
            showLaunchOverlay()
            binding.appBarMain.launchEnterButton.setOnClickListener {
                runLaunchCurtainAnimation()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment).navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        if (!introDismissed) {
            showLaunchOverlay()
        }
    }

    override fun onPause() {
        binding.appBarMain.launchTileWall.pauseAnimation()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_INTRO_DISMISSED, introDismissed)
    }

    private fun showLaunchOverlay() {
        binding.appBarMain.launchOverlay.visibility = View.VISIBLE
        binding.appBarMain.launchOverlay.alpha = 1f
        binding.appBarMain.launchContent.alpha = 1f
        binding.appBarMain.launchEnterButton.isEnabled = true
        binding.appBarMain.curtainLeft.scaleX = 0f
        binding.appBarMain.curtainRight.scaleX = 0f
        binding.appBarMain.launchTileWall.post {
            binding.appBarMain.launchTileWall.resumeAnimation()
        }
    }

    private fun hideLaunchOverlay() {
        binding.appBarMain.launchOverlay.visibility = View.GONE
        binding.appBarMain.launchOverlay.alpha = 0f
        binding.appBarMain.launchTileWall.pauseAnimation()
    }

    private fun runLaunchCurtainAnimation() {
        binding.appBarMain.launchEnterButton.isEnabled = false
        binding.appBarMain.launchContent.animate().alpha(0f).setDuration(180L).start()

        val leftCurtain = binding.appBarMain.curtainLeft
        val rightCurtain = binding.appBarMain.curtainRight
        leftCurtain.pivotX = 0f
        rightCurtain.pivotX = rightCurtain.width.toFloat().coerceAtLeast(1f)

        leftCurtain.animate()
            .scaleX(20f)
            .setDuration(320L)
            .withEndAction {
                binding.appBarMain.launchTileWall.pauseAnimation()
                leftCurtain.animate().scaleX(0f).setDuration(320L).start()
                rightCurtain.animate()
                    .scaleX(0f)
                    .setDuration(320L)
                    .withEndAction {
                        introDismissed = true
                        hideLaunchOverlay()
                    }
                    .start()
            }
            .start()

        rightCurtain.animate()
            .scaleX(20f)
            .setDuration(320L)
            .start()
    }

    companion object {
        private const val KEY_INTRO_DISMISSED = "intro_dismissed"
    }
}
