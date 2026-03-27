package com.example.tarotreader

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import com.example.tarotreader.databinding.ActivityMainBinding
import com.example.tarotreader.ui.background.MysticBackgroundImages
import com.example.tarotreader.ui.horoscope.HoroscopeData
import com.example.tarotreader.ui.horoscope.HoroscopeFragment
import com.example.tarotreader.ui.horoscope.HoroscopeIconFactory

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

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
        binding.appBarMain.contentMain.appBackgroundView.setImages(MysticBackgroundImages.all)
        binding.appBarMain.contentMain.appBackgroundView.showRandomImage()
        refreshBottomNavIcons()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment).navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        refreshBottomNavIcons()
    }

    fun refreshBottomNavIcons() {
        val bottomNav = binding.appBarMain.contentMain.bottomNavView ?: return
        val preferences = getSharedPreferences(HoroscopeFragment.PREFS_NAME, MODE_PRIVATE)
        val savedSign = preferences.getString(
            HoroscopeFragment.KEY_LAST_SIGN,
            HoroscopeData.signs.first()
        ) ?: HoroscopeData.signs.first()

        bottomNav.menu.findItem(R.id.nav_tarot)?.icon = HoroscopeIconFactory.createTarotIcon(this)
        bottomNav.menu.findItem(R.id.nav_horoscope)?.icon =
            HoroscopeIconFactory.createHoroscopeIcon(this, savedSign)
    }
}
