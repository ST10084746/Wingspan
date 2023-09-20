package com.example.wingspan

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wingspan.Models.Observation
import com.example.wingspan.Models.ObservationItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var recyclerView: RecyclerView
private lateinit var observations: ArrayList<ObservationItem>
private lateinit var retroClient: Retro
private lateinit var sightingAdapter: SightingAdapter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recView);

        recyclerView.layoutManager = LinearLayoutManager(this);

        observations = arrayListOf();
        sightingAdapter = SightingAdapter(observations)
        recyclerView.adapter = sightingAdapter

        retroClient = Retro();
        getSightings()
    }
    private fun loadRecView(observations: ArrayList<ObservationItem>){



    }

    private fun getSightings(){
        val latitude = -29.88
        val logiuitute = 31.04
        val distance = 10
        val maxResults = 10
        val api = retroClient.retrofit.create(EBirdApiService::class.java)
        val call = api.getBirdData(latitude,logiuitute,distance,maxResults)

        call.enqueue(object : Callback<Observation>{
            override fun onResponse(call: Call<Observation>, response: Response<Observation>) {
                if(!response.isSuccessful){
                    Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure")
                }
               observations = response.body()!!

                sightingAdapter.notifyDataSetChanged()



               // if (observations != null) {
                 //   loadRecView(observations)
                //}

                observations.forEach{ ob ->
                    Log.e(TAG, "resposnses: " + ob.comName)
                    println(ob.locName)
                }
            }

            override fun onFailure(call: Call<Observation>, t: Throwable) {
               Log.e(TAG, "on Failure " + t.message)
            }

        })


    }
}