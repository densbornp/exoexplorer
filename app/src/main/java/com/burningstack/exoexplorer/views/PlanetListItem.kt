package com.burningstack.exoexplorer.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.burningstack.exoexplorer.R
import com.burningstack.exoexplorer.model.Planet

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
        planetName.text = planet.name
        planetDistance.text = planet.distanceLightYear
        planetTemperature.text = planet.temperature
        planetMass.text = planet.mass
        planetRadius.text = planet.radius
    }

}