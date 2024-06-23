package com.burningstack.exoexplorer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.burningstack.exoexplorer.model.Planet
import com.burningstack.exoexplorer.utils.FragmentTags
import java.util.concurrent.Executors

class PlanetDetailsFragment(val planet: Planet) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planet_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener {
            val frag = parentFragmentManager.findFragmentByTag(FragmentTags.PLANET_LIST_FRAGMENT.value)
            parentFragmentManager.beginTransaction().remove(this).show(frag!!).commit()
        }
        val planetName = view.findViewById<TextView>(R.id.planet_name)
        planetName.text = planet.name
        val planetDistance = view.findViewById<TextView>(R.id.planet_distance)
        planetDistance.text = planet.distanceLightYear
        val planetMass = view.findViewById<TextView>(R.id.planet_mass)
        planetMass.text = planet.mass
        val planetTemperature = view.findViewById<TextView>(R.id.planet_temperature)
        planetTemperature.text = planet.temperature
        val planetSemiMajorAxis = view.findViewById<TextView>(R.id.planet_semi_major_axis)
        planetSemiMajorAxis.text = planet.semiMajorAxis
        val planetRadius = view.findViewById<TextView>(R.id.planet_radius)
        planetRadius.text = planet.radius
        val planetPeriod = view.findViewById<TextView>(R.id.planet_period)
        planetPeriod.text = planet.period
        val planetHotStarMass = view.findViewById<TextView>(R.id.planet_hot_star_mass)
        planetHotStarMass.text = planet.hostStarMass
        val planetHotStarTemperature = view.findViewById<TextView>(R.id.planet_hot_star_temperature)
        planetHotStarTemperature.text = planet.hostStarTemperature
        val planetThumbnail = view.findViewById<ImageView>(R.id.app_bar_image)
        getPlanetThumbnail(planetThumbnail, planet.name.toString())
    }

    // Load planet thumbnail from Wikipedia
    private fun getPlanetThumbnail(imageView: ImageView, planetName: String) {
        ImageLoader(requireActivity(), imageView, planetName)
    }
}