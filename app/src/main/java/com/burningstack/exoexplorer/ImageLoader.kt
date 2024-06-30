package com.burningstack.exoexplorer

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.Executors

class ImageLoader(private val activity: Activity, private val imageView: ImageView, planetName: String) {

    private val baseURL = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&redirects&pithumbsize=250"
    private var client = OkHttpClient()
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    init {
        val name = validateAndFormatName(planetName)
        val requestURL = "$baseURL&titles=$name"
        createInfoRequest(requestURL)
    }

    /*
        This function is used to format some planet names,
        so that they can be found on Wikipedia
     */
    private fun validateAndFormatName(name: String): String {
        val oldName = name.lowercase()
        if (oldName == "mercury") {
            return "Mercury_(planet)"
        }
        return name
    }

    private fun getThumbnailURL(value: JsonObject): String {
        try {
            val pages = value.getAsJsonObject("query").getAsJsonObject("pages")
            val pageID = pages.keySet().first()
            val page = pages.getAsJsonObject(pageID)
            try {
                val thumbnailURL = page.getAsJsonObject("thumbnail").get("source").asString
                Log.i("ImageLoader", "Thumbnail URL: $thumbnailURL")
                return thumbnailURL
            } catch (e: Exception) {
                // Check for redirects
                val redirects = value.getAsJsonObject("query").getAsJsonArray("redirects")
                val redirect = redirects.get(0).asJsonObject
                val to = redirect.get("to").asString
                val newURL = "$baseURL&titles=$to"
                Log.i("ImageLoader", "New thumbnail URL: $newURL")
                return newURL
            }
        } catch (e: Exception) {
            // No redirects and no thumbnail
            Log.i("ImageLoader", "Error loading thumbnail URL")
            return ""
        }
    }

    private fun createInfoRequest(requestURL: String) {
        executor.execute {
            val request = Request.Builder()
                .url(requestURL)
                .build()
            val response: Response
            try {
                response = client.newCall(request).execute()
            } catch (e: Exception) {
                throw Exception("Error while executing request: $e")
            }

            response.use { resp ->
                if (resp.isSuccessful) {
                    val result = JsonParser.parseString(resp.body?.string().toString()).asJsonObject
                    createThumbnailRequest(getThumbnailURL(result))
                }
            }
        }
    }

    private fun createThumbnailRequest(requestURL: String) {
        val downloader = OkHttp3Downloader(client)
        val picasso = Picasso.Builder(activity)
            .downloader(downloader)
            .build()
        if (requestURL == "") {
            handler.post {
                imageView.setImageResource(R.drawable.error)
            }
        } else {
            handler.post {
                picasso.load(requestURL).placeholder(R.drawable.downloading).error(R.drawable.error).into(imageView)
            }
        }
    }
}