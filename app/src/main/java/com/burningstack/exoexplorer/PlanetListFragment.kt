package com.burningstack.exoexplorer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import com.burningstack.exoexplorer.utils.FragmentTags
import com.burningstack.exoexplorer.views.PlanetListItem
import org.w3c.dom.Text
import java.util.concurrent.Executors

class PlanetListFragment : Fragment() {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchView: SearchView
    private lateinit var noDataTextField: TextView
    private lateinit var context: Context
    private lateinit var fragManager: FragmentManager

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
        fragManager = parentFragmentManager
        noDataTextField = view.findViewById(R.id.tv_no_data)
        searchView = view.findViewById(R.id.searchView)
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
            val apiConnector = APIConnector(requireActivity())
            executor.execute {
                val result = apiConnector.getPlanets(query)

                handler.post {
                    val linearLayout = view.findViewById<LinearLayout>(R.id.planet_list)
                    linearLayout.removeAllViews()
                    if (result.isNotEmpty()) {
                        noDataTextField.visibility = View.GONE
                        for (planet in result) {
                            val listItem = PlanetListItem(requireContext(), planet)
                            // Set list item onClickListener
                            listItem.setOnClickListener {
                                val activeFragment = getActiveFragment()
                                val planetDetailsFragment = PlanetDetailsFragment(planet)
                                if (activeFragment != null) {
                                    fragManager.beginTransaction().hide(activeFragment)
                                        .add(R.id.fragment_container, planetDetailsFragment, FragmentTags.PLANET_DETAILS_FRAGMENT.value)
                                        .show(planetDetailsFragment)
                                        .commit()
                                }

                            }
                            linearLayout.addView(listItem)
                        }
                    } else {
                        noDataTextField.text = getString(R.string.no_data_found)
                        noDataTextField.visibility = View.VISIBLE
                    }
                }
            }
        }
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