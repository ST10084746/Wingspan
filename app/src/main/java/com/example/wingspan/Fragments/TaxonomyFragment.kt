package com.example.wingspan.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.wingspan.Models.BirdInfoItem
import com.example.wingspan.R



class TaxonomyFragment : Fragment() {

    private val navigationArgs: TaxonomyFragmentArgs by navArgs()
    private lateinit var info: BirdInfoItem
    private lateinit var tvComName:TextView
    private lateinit var tvCategory:TextView
    private lateinit var tvSciName:TextView
    private lateinit var tvfamSciName:TextView
    private lateinit var tvSpecCode:TextView
    private lateinit var tvFamCode:TextView
    private lateinit var tvFamComName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taxonomy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvComName = view.findViewById(R.id.tvCommNameDetails)
        tvFamCode = view.findViewById(R.id.tvfamCode)
        tvCategory = view.findViewById(R.id.tvcategory)
        tvSciName = view.findViewById(R.id.tvSciName)
        tvFamComName = view.findViewById(R.id.tvfamComName)
        tvSpecCode = view.findViewById(R.id.tvspeciesCode)
        tvfamSciName = view.findViewById(R.id.tvfamSciName)

        tvComName.text = navigationArgs.commonName;
        tvSciName.text = navigationArgs.sciName
        tvSpecCode.text = navigationArgs.specCode
        tvCategory.text = navigationArgs.category
        tvFamComName.text = navigationArgs.famComName
        tvFamCode.text = navigationArgs.famCode
        tvfamSciName.text = navigationArgs.famSciName

        super.onViewCreated(view, savedInstanceState)
    }
}