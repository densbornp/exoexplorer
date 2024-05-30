package com.burningstack.exoexplorer.model

class Planet() {
   // the name of the planet
   var name: String = ""
   // mass of the planet in Jupiters (1 Jupiter = 1.898 × 1027 kg)
   var mass: Double = 0.0
   // average radius of the planet in Jupiters (1 Jupiter = 69911 km)
   var radius: Double = 0.0
   // orbital period of the planet in Earth days
   var period: Double = 0.0
   // average semi major axis of planet in astronomical units (AU)
   var semiMajorAxis: Double = 0.0
   // average surface temperature of the planet in Kelvin
   var temperature: Double = 0.0
   // distance the planet is from Earth in light years
   var distanceLightYear: Double = 0.0
   // hot star mass in solar mass (1 solar mass = 1.989 × 1030 kg)
   var hostStarMass: Double = 0.0
   // hot star temperature in Kelvin
   var hostStarTemperature: Double = 0.0

   constructor(name: String, mass: Double, radius: Double, period: Double, semiMajorAxis: Double, temperature: Double, distanceLightYear: Double, hostStarMass: Double, hostStarTemperature: Double) : this() { this.name = name
      this.mass = mass
      this.radius = radius
      this.period = period
      this.semiMajorAxis = semiMajorAxis
      this.temperature = temperature
      this.distanceLightYear = distanceLightYear
      this.hostStarMass = hostStarMass
      this.hostStarTemperature = hostStarTemperature
   }
}