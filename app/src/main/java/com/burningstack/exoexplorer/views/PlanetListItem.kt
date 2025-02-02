package com.burningstack.exoexplorer.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.burningstack.exoexplorer.R
import com.burningstack.exoexplorer.db.AppDatabaseSingleton
import com.burningstack.exoexplorer.model.Planet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("ViewConstructor")
class PlanetListItem(context: Context, planet: Planet): ConstraintLayout(context) {
    init {
        // Inflate the view
        View.inflate(context, R.layout.planet_list_item, this)
        val planetName = findViewById<TextView>(R.id.planetName)
        val planetDistance = findViewById<TextView>(R.id.item_distance)
        val planetTemperature = findViewById<TextView>(R.id.item_temperature)
        val planetMass = findViewById<TextView>(R.id.item_mass)
        val planetRadius = findViewById<TextView>(R.id.item_radius)
        val favorite = findViewById<ImageView>(R.id.img_fav)
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabaseSingleton.getInstance(context)
            val favoriteDao = database.favoritePlanetDao()
            if (favoriteDao.isFavorite(planet.name)) {
                withContext(Dispatchers.Main) {
                    favorite.setImageResource(R.drawable.favorite)
                }
            } else {
                withContext(Dispatchers.Main) {
                    favorite.setImageResource(R.drawable.favorite_border)
                }
            }
        }
        favorite.setOnClickListener {
            toggleFavorite(favorite, planet)
        }
        planetName.text = planet.name
        planetDistance.text = planet.distanceLightYear
        planetTemperature.text = planet.temperature
        planetMass.text = planet.mass
        planetRadius.text = planet.radius

    }

    private fun toggleFavorite(image: ImageView, planet: Planet) {
        // TODO click on fav icon should directly update the view
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabaseSingleton.getInstance(context)
            val favoriteDao = database.favoritePlanetDao()
            if (favoriteDao.isFavorite(planet.name)) {
                favoriteDao.removeFavorite(planet)
                withContext(Dispatchers.Main) {
                    image.setImageResource(R.drawable.favorite_border)
                }
            } else {
                favoriteDao.addFavorite(planet)
                withContext(Dispatchers.Main) {
                    image.setImageResource(R.drawable.favorite)
                }
            }
        }
    }

}