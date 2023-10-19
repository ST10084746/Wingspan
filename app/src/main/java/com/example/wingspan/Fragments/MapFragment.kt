package com.example.wingspan.Fragments

import android.content.ContentValues
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.wingspan.EBirdApiService
import com.example.wingspan.Models.Hotspot
import com.example.wingspan.R
import com.example.wingspan.Retro
import com.example.wingspan.Sighting
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        hotspots = arrayListOf()
        prefs = PreferenceManager.getDefaultSharedPreferences(this.requireContext())


        getHotSpots()
    }
    private  fun  addToList(hotspot: Hotspot){
        hotspots.add(hotspot)
    }

    private fun getRange(): Int{

        val distanceString = prefs.getString("range", "25")
        var distance = 0
        val switch = prefs.getBoolean("unit", false)

        var multiplier = 0.0

        if (distanceString!!.toInt()==null){
            distance = 25
        }
        else {
            distance = distanceString!!.toInt()
        }



        if(switch){
            multiplier = 1.6
        } else{
            multiplier = 1.0
        }

        var range = distance*multiplier.toInt()

        if (range>50){
            range=50;
        }

        return range
    }

     private fun getHotSpots(){
        val latitude = -29.88
        val longitude = 31.04
        val distance = getRange()
        val fmt = "json"

         retroClient= Retro()
        val api = retroClient.retrofit.create(EBirdApiService::class.java)
        val call = api.getBirdData(latitude,longitude,distance,fmt)

        call.enqueue(object : Callback<ArrayList<Hotspot>>{
            override fun onResponse(
                call: Call<ArrayList<Hotspot>>,
                response: Response<ArrayList<Hotspot>>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure")
                }
                response.body()!!.forEach{ hotspot->
                    addToList(hotspot)
                }

                hotspots.forEach{ ob ->
                    Log.e(ContentValues.TAG, "responses: " + ob.locName)
                    println(ob.locName)
                }
                Log.e(ContentValues.TAG, "responses: " + distance )
                println(distance)
            }

            override fun onFailure(call: Call<ArrayList<Hotspot>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.message.toString())
            }

        })



    }
}