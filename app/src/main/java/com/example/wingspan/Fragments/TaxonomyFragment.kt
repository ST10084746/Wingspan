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
        info = navigationArgs.BirdInfo
        tvComName = view.findViewById(R.id.tvCommNameDetails)
        tvComName.text = info.comName;

        super.onViewCreated(view, savedInstanceState)
    }
}