package com.burningstack.exoexplorer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import com.burningstack.exoexplorer.views.PlanetListItem
import java.util.concurrent.Executors

class PlanetListFragment : Fragment() {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_planet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.isIconified = false
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchPlanets(view, query.toString())
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchPlanets(view: View, query: String) {
        if (query.isNotEmpty()) {
            val apiConnector = APIConnector(requireContext())
            executor.execute {
                val result = apiConnector.getPlanetsByName(query)

                handler.post {
                    if (result.isNotEmpty()) {
                        // Display the list of planets
                        val linearLayout = view.findViewById<LinearLayout>(R.id.planet_list)
                        linearLayout.removeAllViews()
                        for (planet in result) {
                            val listItem = PlanetListItem(requireContext(), planet)
                            linearLayout.addView(listItem)
                        }
                    }
                }
            }
        }
    }
}