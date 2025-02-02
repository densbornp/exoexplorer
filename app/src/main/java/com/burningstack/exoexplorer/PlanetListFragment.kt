package com.burningstack.exoexplorer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.burningstack.exoexplorer.db.AppDatabaseSingleton
import com.burningstack.exoexplorer.model.Planet
import com.burningstack.exoexplorer.utils.FragmentTags
import com.burningstack.exoexplorer.views.PlanetListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class PlanetListFragment() : Fragment() {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchView: SearchView
    private lateinit var noDataTextField: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var context: Context
    private lateinit var fragManager: FragmentManager
    private var isFavoriteView: Boolean = false
    private var queryTextChangedJob: Job? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_planet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFavoriteView = arguments?.getBoolean("isFavoriteView") ?: false
        fragManager = childFragmentManager
        noDataTextField = view.findViewById(R.id.tv_no_data)
        searchView = view.findViewById(R.id.searchView)
        searchView.isIconified = false
        if (isFavoriteView) {
            searchView.queryHint = getString(R.string.search_view_favs_hint)
        } else {
            searchView.queryHint = getString(R.string.search_view_hint)
        }
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (isFavoriteView) {
                    searchView.clearFocus()
                } else {
                    searchPlanets(view, query.toString())
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (isFavoriteView) {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(250)
                        searchPlanets(view, newText.toString())
                    }
                }
                return false
            }
        })
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        if(isFavoriteView) {
            loadFavorites(view)
        }
    }

    private fun searchPlanets(view: View, query: String) {
        val apiConnector = APIConnector(requireActivity())
        val linearLayout = view.findViewById<LinearLayout>(R.id.planet_list)
        executor.execute {
            handler.post {
                linearLayout.removeAllViews()
                noDataTextField.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }

            var result = ArrayList<Planet>()
            if (isFavoriteView) {
                CoroutineScope(Dispatchers.IO).launch {
                    val database = AppDatabaseSingleton.getInstance(context)
                    val favoriteDao = database.favoritePlanetDao()
                    result = if (query.isNotEmpty()) {
                        favoriteDao.getFavorites(query) as ArrayList<Planet>
                    } else {
                        favoriteDao.getFavorites() as ArrayList<Planet>
                    }
                }
            } else {
                if (query.isNotEmpty()) {
                    result = apiConnector.getPlanets(query)
                }
            }

            handler.post {
                if (result.isNotEmpty()) {
                    progressBar.visibility = View.GONE
                    noDataTextField.visibility = View.GONE
                    for (planet in result) {
                        val listItem = PlanetListItem(requireContext(), planet)
                        // Set list item onClickListener
                        listItem.setOnClickListener {
                            val planetDetailsFragment = PlanetDetailsFragment(planet)
                            fragManager.beginTransaction()
                                .add(R.id.fragment_planet_list, planetDetailsFragment, FragmentTags.PLANET_DETAILS_FRAGMENT.value)
                                .show(planetDetailsFragment)
                                .commit()
                        }
                        linearLayout.addView(listItem)
                    }
                } else {
                    progressBar.visibility = View.GONE
                    if (isFavoriteView) {
                        noDataTextField.text = getString(R.string.no_favs_found)
                    } else {
                        noDataTextField.text = getString(R.string.no_data_found)
                    }
                    noDataTextField.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadFavorites(view: View) {
        val linearLayout = view.findViewById<LinearLayout>(R.id.planet_list)
        executor.execute {
            handler.post {
                linearLayout.removeAllViews()
                noDataTextField.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }

            var result = ArrayList<Planet>()
            CoroutineScope(Dispatchers.IO).launch {
                val database = AppDatabaseSingleton.getInstance(context)
                val favoriteDao = database.favoritePlanetDao()
                result = favoriteDao.getFavorites() as ArrayList<Planet>
            }

            handler.post {
                if (result.isNotEmpty()) {
                    progressBar.visibility = View.GONE
                    noDataTextField.visibility = View.GONE
                    for (planet in result) {
                        val listItem = PlanetListItem(requireContext(), planet)
                        // Set list item onClickListener
                        listItem.setOnClickListener {
                            val planetDetailsFragment = PlanetDetailsFragment(planet)
                            fragManager.beginTransaction()
                                .add(R.id.fragment_planet_list, planetDetailsFragment, FragmentTags.PLANET_DETAILS_FRAGMENT.value)
                                .show(planetDetailsFragment)
                                .commit()

                        }
                        linearLayout.addView(listItem)
                    }
                } else {
                    progressBar.visibility = View.GONE
                    if (isFavoriteView) {
                        noDataTextField.text = getString(R.string.no_favs_found)
                    } else {
                        noDataTextField.text = getString(R.string.no_data_found)
                    }
                    noDataTextField.visibility = View.VISIBLE
                }
            }
        }
    }

    fun updateFavoritesView() {
        view?.let { loadFavorites(it) }
    }

    private fun getActiveFragment(): Fragment? {
        val fragments = fragManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible) {
                return fragment
            }
        }
        return null
    }
}