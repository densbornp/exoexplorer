package com.burningstack.exoexplorer

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.burningstack.exoexplorer.model.Planet
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class APIConnector(activity: Activity) {

    private val baseURL: String = "https://api.api-ninjas.com/v1/planets"
    private val prefs = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
    private val API_KEY: String = prefs.getString(activity.getString(R.string.preferences_api_key), "").toString()
    private var client = OkHttpClient()
    private var act = activity

    fun getPlanetsByName(name: String): ArrayList<Planet> {
        val params = "?name=$name"
        val requestURL = baseURL + params
        if (API_KEY.isEmpty()) {
            return ArrayList()
        }
        val result = createRequest(requestURL)
        return formatResult(result)
    }

    private fun formatResult(result: JsonArray): ArrayList<Planet> {
        val resultList = ArrayList<Planet>()
        if (!result.isEmpty) {
            for (res in result) {
                val planet = Gson().fromJson(res, Planet::class.java)
                resultList.add(planet)
            }
        }
        return resultList
    }

    private fun createRequest(requestURL: String): JsonArray {
        val request = Request.Builder()
            .url(requestURL)
            .header("x-api-key", API_KEY)
            .build()
        val response: Response
        try {
            response = client.newCall(request).execute()
        } catch (e: Exception) {
            throw Exception("Error while executing request: $e")
        }

        response.use { response ->
            if (!response.isSuccessful) {
                showAPIKeyErrorDialog()
                return JsonArray()
            }
            return JsonParser.parseString(response.body?.string().toString()).asJsonArray
        }
    }

    private fun showAPIKeyErrorDialog() {
        act.runOnUiThread {
            val builder = AlertDialog.Builder(act)
            builder.setIcon(R.drawable.dialog_error)
            builder.setTitle(R.string.api_key_error_title)
            builder.setMessage(R.string.api_key_error_msg)
                .setPositiveButton(R.string.btn_ok) { dialog, id ->
                    // Do nothing
                }
            builder.show()
        }
    }

    /*
       Possible parameters for web requests
       minimum mass of the planet in Jupiters (1 Jupiter = 1.898 × 1027 kg)
       * min_mass
       maximum mass of the planet in Jupiters (1 Jupiter = 1.898 × 1027 kg)
       * max_mass
       minimum average radius of the planet in Jupiters (1 Jupiter = 69911 km)
       * min_radius
       maximum average radius of the planet in Jupiters (1 Jupiter = 69911 km)
       * max_radius
       minimum orbital period of the planet in Earth days
       * min_period
       maximum orbital period of the planet in Earth days
       * max_period
       minimum average surface temperature of the planet in Kelvin
       * min_temperature
       maximum average surface temperature of the planet in Kelvin
       * max_temperature
       minimum distance the planet is from Earth in light years
       * min_distance_light_year
       maximum distance the planet is from Earth in light years
       * max_distance_light_year
       minimum semi major axis of planet in astronomical units (AU)
       * min_semi_major_axis
       maximum semi major axis of planet in astronomical units (AU)
       * max_semi_major_axis
       number of results to offset for pagination
       * offset
   */
}