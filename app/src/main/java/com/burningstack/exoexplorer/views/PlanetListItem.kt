package com.burningstack.exoexplorer.views

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.burningstack.exoexplorer.R
import com.burningstack.exoexplorer.model.Planet

class PlanetListItem(context: Context, planet: Planet): ConstraintLayout(context) {
    init {
        // Inflate the view
        View.inflate(context, R.layout.planet_list_item, this)
        var planetImage = findViewById<ImageView>(R.id.planetImage)
        var planetName = findViewById<TextView>(R.id.planetName)
        planetName.text = planet.name
    }

}