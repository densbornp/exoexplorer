package com.burningstack.exoexplorer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.burningstack.exoexplorer.databinding.ActivityMainBinding
import com.burningstack.exoexplorer.utils.FragmentTags
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private val fragManager = supportFragmentManager

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
            add(R.id.fragment_container, planetListFragment, FragmentTags.PLANET_LIST_FRAGMENT.value)
            add(R.id.fragment_container, profileFragment, FragmentTags.PROFILE_FRAGMENT.value)
            add(R.id.fragment_container, settingsFragment, FragmentTags.SETTINGS_FRAGMENT.value)
            hide(profileFragment)
            hide(settingsFragment)
            commit()
        }
        // Check if API key is set
        if (apiKey.isNullOrEmpty()) {
            bottomNav.setSelectedItemId(R.id.nav_settings)
            showFragment(settingsFragment)

        } else {
            bottomNav.setSelectedItemId(R.id.nav_discover)
            showFragment(planetListFragment)
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_discover -> {
                    showFragment(planetListFragment)
                    true
                }
                R.id.nav_profile -> {
                    showFragment(profileFragment)
                    true
                }
                R.id.nav_settings -> {
                    showFragment(settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = fragManager.beginTransaction()
        val fragments = fragManager.fragments
        for (frag in fragments) {
            transaction.hide(frag)
        }

        if (fragment.tag.toString() == FragmentTags.PLANET_LIST_FRAGMENT.value) {
            val detailsFrag = fragManager.findFragmentByTag(FragmentTags.PLANET_DETAILS_FRAGMENT.value)
            if (detailsFrag != null) {
                transaction.show(detailsFrag)
            } else {
                transaction.show(fragment)
            }
        } else {
            transaction.show(fragment)
        }
        transaction.commit()
    }

}