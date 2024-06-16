package com.burningstack.exoexplorer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import com.burningstack.exoexplorer.utils.FragmentTags
import com.burningstack.exoexplorer.views.PlanetListItem

class PlanetDetailsFragment(val planetListItem: PlanetListItem) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planet_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            val frag = parentFragmentManager.findFragmentByTag(FragmentTags.PLANET_LIST_FRAGMENT.value)
            parentFragmentManager.beginTransaction().remove(this).show(frag!!).commit()
        }
    }
}