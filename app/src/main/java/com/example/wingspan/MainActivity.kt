package com.example.wingspan

import android.content.ContentValues.TAG
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wingspan.Models.Observation
import com.example.wingspan.Models.ObservationItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var countDownTimer: CountDownTimer
private lateinit var recyclerView: RecyclerView
private lateinit var observations: ArrayList<ObservationItem>
private lateinit var retroClient: Retro
private lateinit var sightingAdapter: SightingAdapter
private lateinit var blackScreen: View
private lateinit var progressBar: ProgressBar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observations = arrayListOf();
        sightingAdapter = SightingAdapter(observations)


        retroClient = Retro();
        getSightings()
    }

    private fun fadeIn(){
        blackScreen = findViewById(R.id.v_blackScreen)
        blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }
    private fun recyclerViewSetup(){
        recyclerView = findViewById(R.id.recView);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = sightingAdapter
    }

    private  fun  addToList(observationItem: ObservationItem){
        observations.add(observationItem)
    }

    private fun getSightings(){
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val latitude = -29.88
        val logiuitute = 31.04
        val distance = 10
        val maxResults = 10
        val api = retroClient.retrofit.create(EBirdApiService::class.java)
        val call = api.getBirdData(latitude,logiuitute,distance,maxResults)

        call.enqueue(object : Callback<ArrayList<ObservationItem>>{
            override fun onResponse(
                call: Call<ArrayList<ObservationItem>>,
                response: Response<ArrayList<ObservationItem>>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure")
                }
                response.body()!!.forEach{ ob->
                    addToList(ob)
                }
                recyclerViewSetup()
                fadeIn()
                progressBar.visibility = View.GONE

                sightingAdapter.notifyDataSetChanged()



                // if (observations != null) {
                //   loadRecView(observations)
                //}

                observations.forEach{ ob ->
                    Log.e(TAG, "responses: " + ob.comName)
                    println(ob.locName)
                }
            }

            override fun onFailure(call: Call<ArrayList<ObservationItem>>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                attemptRequest()

            }


        })


    }

    private fun attemptRequest() {
        countDownTimer = object : CountDownTimer(5*1000,1000){
            override fun onTick(p0: Long) {
                Log.i("MainActivity", "Trying again in ${p0/1000} seconds")
            }

            override fun onFinish() {
                getSightings()
                countDownTimer.cancel()
            }

        }
        countDownTimer.start()
    }
}