package com.example.wingspan.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wingspan.R
import com.example.wingspan.Retro
import com.example.wingspan.Models.Sighting
import com.example.wingspan.SightingAdapter
import com.google.firebase.firestore.FirebaseFirestore


class observationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var observations: ArrayList<Sighting>
    private lateinit var retroClient: Retro
    private lateinit var sightingAdapter: SightingAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_observation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observations = arrayListOf();
        recyclerView = view.findViewById(R.id.recView);
        recyclerView.layoutManager = LinearLayoutManager(context)

        sightingAdapter = SightingAdapter(observations)

        recyclerView.adapter = sightingAdapter


       // retroClient = Retro();
        loadObservations()
        super.onViewCreated(view, savedInstanceState)

    }





    private fun loadObservations(){
        db = FirebaseFirestore.getInstance()

        db.collection("Observations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    observations.add(document.toObject(Sighting:: class.java))
                }

                sightingAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener{exception->
                Log.w(ContentValues.TAG, "Error getting Documents.", exception)
            }
    }

}