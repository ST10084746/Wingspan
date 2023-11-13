package com.example.wingspan.Fragments

import android.app.Dialog
import android.content.ContentValues
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.wingspan.EBirdApiService
import com.example.wingspan.Models.Hotspot
import com.example.wingspan.R
import com.example.wingspan.Retro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddObservationFragment : Fragment() {

    private lateinit var btnUploadSighting: Button
    private lateinit var hotSpotSpinner: Spinner
    private lateinit var etSciName: EditText
    private lateinit var etComName : EditText
    private lateinit var etSpecCode : EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var retroClient: Retro
    private lateinit var hotspots: ArrayList<Hotspot>
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_observation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()

        hotspots = arrayListOf()
        hotSpotSpinner = view.findViewById(R.id.hotSpotSpinner)

        getHotSpots()

        val userID = auth.currentUser?.uid

        if (userID != null) {

            Log.d("User ID", userID)
            btnUploadSighting = view.findViewById(R.id.btn_uploadSighting)
            etComName = view.findViewById(R.id.et_comName)
            etSpecCode = view.findViewById(R.id.et_specCode)
            etSciName = view.findViewById(R.id.et_sciName)

            btnUploadSighting.setOnClickListener {
                var sighting : MutableMap<String, Any> = HashMap()
                sighting["speciesCode"] = etSpecCode.text.toString()
                sighting["sciName"] = etSciName.text.toString()
                sighting["comName"] = etComName.text.toString()
                sighting["userID"] = userID.toString()

                db.collection("Observations")
                    .add(sighting)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Observation Added", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        };
        else{

            Toast.makeText(context, "Please login to add an observation.", Toast.LENGTH_SHORT).show()
        }
    }

    private  fun  addToList(hotspot: Hotspot){
        hotspots.add(hotspot)
    }
    private fun getHotSpots() {
        val latitude = -29.88
        val longitude = 31.04
        val distance = 25
        val fmt = "json"

        retroClient = Retro()
        val api = retroClient.retrofit.create(EBirdApiService::class.java)
        val call = api.getBirdData(latitude, longitude, distance, fmt)

        call.enqueue(object : Callback<ArrayList<Hotspot>> {
            override fun onResponse(
                call: Call<ArrayList<Hotspot>>,
                response: Response<ArrayList<Hotspot>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure")
                    return
                }

                hotspots = response.body() ?: ArrayList()

                hotspots.forEach { ob ->
                    Log.e(ContentValues.TAG, "Add Obs Responses: " + ob.locName)
                    println(ob.locName)
                }

                // Populate the Spinner with the hotspots
                populateSpinner()
            }

            override fun onFailure(call: Call<ArrayList<Hotspot>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun populateSpinner() {
        val hotspotNames = hotspots.map { it.locName }.distinct()

        val spinnerItems = listOf("None") + hotspotNames.distinct()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerItems.toTypedArray()
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val hotSpotSpinner: Spinner = view?.findViewById(R.id.hotSpotSpinner) ?: return
        hotSpotSpinner.adapter = adapter
    }
}