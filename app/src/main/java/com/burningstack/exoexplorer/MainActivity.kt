package com.burningstack.exoexplorer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.burningstack.exoexplorer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private val fragManager = supportFragmentManager
    private var activeFragment: Fragment = PlanetListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val apiKey = prefs.getString(getString(R.string.preferences_api_key), "")

        bottomNav = findViewById(R.id.btmNavMenu)
        val planetListFragment = PlanetListFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()
        fragManager.beginTransaction().apply {
            add(R.id.fragment_container, planetListFragment)
            add(R.id.fragment_container, profileFragment)
            add(R.id.fragment_container, settingsFragment)
            hide(profileFragment)
            hide(settingsFragment)
            activeFragment = planetListFragment
            commit()
        }
        // Check if API key is set
        if (apiKey.isNullOrEmpty()) {
            bottomNav.setSelectedItemId(R.id.nav_settings)
            fragManager.beginTransaction().hide(activeFragment)
                .show(settingsFragment).commit()
            activeFragment = settingsFragment
        } else {
            bottomNav.setSelectedItemId(R.id.nav_discover)
            fragManager.beginTransaction().hide(activeFragment)
                .show(planetListFragment).commit()
            activeFragment = planetListFragment
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_discover -> {
                    fragManager.beginTransaction().hide(activeFragment)
                        .show(planetListFragment).commit()
                    activeFragment = planetListFragment
                    true
                }
                R.id.nav_profile -> {
                    fragManager.beginTransaction().hide(activeFragment)
                        .show(profileFragment).commit()
                    activeFragment = profileFragment
                    true
                }
                R.id.nav_settings -> {
                    fragManager.beginTransaction().hide(activeFragment)
                        .show(settingsFragment).commit()
                    activeFragment = settingsFragment
                    true
                }
                else -> false
            }
        }
    }

}