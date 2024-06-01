package com.burningstack.exoexplorer.model

import com.google.gson.annotations.SerializedName

class Planet() {
   // the name of the planet
   @SerializedName("name")
   var name: String? = ""
   // mass of the planet in Jupiters (1 Jupiter = 1.898 × 1027 kg)
   @SerializedName("mass")
   var mass: String? = "N.A"
   // average radius of the planet in Jupiters (1 Jupiter = 69911 km)
   @SerializedName("radius")
   var radius: String? = "N.A"
   // orbital period of the planet in Earth days
   @SerializedName("period")
   var period: String? = "N.A"
   // average semi major axis of planet in astronomical units (AU)
   @SerializedName("semi_major_axis")
   var semiMajorAxis: String? = "N.A"
   // average surface temperature of the planet in Kelvin
   @SerializedName("temperature")
   var temperature: String? = "N.A"
   // distance the planet is from Earth in light years
   @SerializedName("distance_light_year")
   var distanceLightYear: String? = "N.A"
   // hot star mass in solar mass (1 solar mass = 1.989 × 1030 kg)
   @SerializedName("host_star_mass")
   var hostStarMass: String? = "N.A"
   // hot star temperature in Kelvin
   @SerializedName("host_star_temperature")
   var hostStarTemperature: String? = "N.A"

   constructor(name: String, mass: String?, radius: String?, period: String?, semiMajorAxis: String?, temperature: String?, distanceLightYear: String?, hostStarMass: String?, hostStarTemperature: String?) : this() {
      this.name = name
      this.mass = mass ?: "N.A"
      this.radius = radius ?: "N.A"
      this.period = period ?: "N.A"
      this.semiMajorAxis = semiMajorAxis ?: "N.A"
      this.temperature = temperature ?: "N.A"
      this.distanceLightYear = distanceLightYear ?: "N.A"
      this.hostStarMass = hostStarMass ?: "N.A"
      this.hostStarTemperature = hostStarTemperature ?: "N.A"
   }
}