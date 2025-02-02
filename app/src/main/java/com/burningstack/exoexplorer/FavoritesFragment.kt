package com.burningstack.exoexplorer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burningstack.exoexplorer.utils.FragmentTags


/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private lateinit var planetListFragment: PlanetListFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        planetListFragment = PlanetListFragment()
        val args = Bundle()
        args.putBoolean("isFavoriteView", true)
        planetListFragment.arguments = args
        childFragmentManager.beginTransaction()
            .add(R.id.fragFavorites,
                planetListFragment,
                FragmentTags.PLANET_LIST_FRAGMENT.value)
            .replace(R.id.fragFavorites, planetListFragment)
            .commit()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            planetListFragment.updateFavoritesView()
        }
    }
}