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
<<<<<<< Updated upstream
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


=======

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        return view

    }


    private fun getDirections(startLat: Double, startLong: Double, endLat: Double, endLong: Double) {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ua = WebView(requireContext()).settings.userAgentString
        val roadManager: RoadManager = OSRMRoadManager(requireContext(), ua)

        val waypoints = ArrayList<GeoPoint>()

        val startPoint = GeoPoint(startLat, startLong)
        val endPoint = GeoPoint(endLat, endLong)

        waypoints.add(startPoint)
        waypoints.add(endPoint)

        val road = roadManager.getRoad(waypoints)
        val roadOverlay = RoadManager.buildRoadOverlay(road)
        val wingSpanRed = Color.parseColor("#e83c34")

        roadOverlay.outlinePaint.color = wingSpanRed
        roadOverlay.outlinePaint.strokeWidth = 10f

        // Display route steps as markers
        val roadSteps = road.mNodes // List of route steps

        for (i in 0 until roadSteps.size) {
            val step = roadSteps[i]

            // Check if step.mInstructions is not null and not empty
            if (!step.mInstructions.isNullOrBlank()) {
                val stepMarker = Marker(mapView)
                stepMarker.position = step.mLocation
                stepMarker.title = step.mInstructions
                stepMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.directions) // Customize the marker icon
                mapView.overlays.add(stepMarker)

            }
        }

        val last = mapView.overlays.last()
        mapView.getOverlays().remove(last);
        mapView.overlays.add(roadOverlay)

        // Calculate the bounding box to fit the overlay
        val boundingBox = roadOverlay.bounds
        // Center the map on the bounding box
        mapView.controller.setCenter(boundingBox.centerWithDateLine)
        // Zoom to fit the bounding box with padding (adjust as needed)
        mapView.zoomToBoundingBox(boundingBox, true, 50)

        mapView.invalidate()
    }

    private fun getRouteInstructions(startLat: Double, startLong: Double, endLat: Double, endLong: Double): ArrayList<String> {

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ua = WebView(requireContext()).settings.userAgentString
        val roadManager: RoadManager = OSRMRoadManager(requireContext(), ua)
        val waypoints = ArrayList<GeoPoint>()

        val startPoint = GeoPoint(startLat, startLong)
        val endPoint = GeoPoint(endLat, endLong)

        waypoints.add(startPoint)
        waypoints.add(endPoint)

        val road = roadManager.getRoad(waypoints)
        val routeSteps = ArrayList<String>()

        for (i in 0 until road.mNodes.size) {
            val step = road.mNodes[i]
            if (!step.mInstructions.isNullOrBlank()) {
                routeSteps.add(step.mInstructions)
            }
        }

        return routeSteps
    }

    private fun showRouteInstructionsDialog(routeInstructions: ArrayList<String>, targetPoint: GeoPoint ) {
        // Create a custom dialog
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_route_steps) // Set the CardView layout
        val window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.white)
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Find the RecyclerView in the CardView layout
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.routeStepsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up the adapter for the RecyclerView with the route instructions
        val adapter = RouteInstructionsAdapter(routeInstructions)
        recyclerView.adapter = adapter

        // Find the "Begin Navigation" button and set up a click listener
        val beginNavigationButton = dialog.findViewById<Button>(R.id.beginNavigationButton)
        beginNavigationButton.setOnClickListener {
            // Handle the "Begin Navigation" button click
            mapController.setCenter(targetPoint)
            mapController.setZoom(20.0);
            // You can start the navigation here
            dialog.dismiss() // Close the dialog
        }
        // Show the dialog
        dialog.show()
    }

>>>>>>> Stashed changes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        hotspots = arrayListOf()
        prefs = PreferenceManager.getDefaultSharedPreferences(this.requireContext())


<<<<<<< Updated upstream
=======
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        mapView =  view.findViewById(R.id.mvOne)

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.mapCenter
        mapView.setMultiTouchControls(true)
        mapView.getLocalVisibleRect(Rect())
        mapController = mapView.controller


        //set the initial zoom
        mapController.setZoom(20.0)

        managePermissions()

        mapView.setMultiTouchControls(true)

        myLocationOverLay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        myLocationOverLay.enableMyLocation()
        //init the start point
        val geoPoint = myLocationOverLay.myLocation
        val startPoint = GeoPoint(-29.8393456,30.8427937)
        mapController.setCenter(startPoint)
        mapController.setZoom(15.0)

        //create marker

        val marker = Marker(mapView)
        marker.position = startPoint

        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_location_user, null)

        //add click listener to marker

        marker.setOnMarkerClickListener { marker, mapView ->

            val latitude = marker.position.latitude
            val longitude = marker.position.longitude
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.custom)

            val window = dialog.window
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setGravity(Gravity.BOTTOM)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val nameTextView = dialog.findViewById<TextView>(R.id.nameTextView)
            val directionsButton = dialog.findViewById<Button>(R.id.directionsButton)
            val longitudeTextView = dialog.findViewById<TextView>(R.id.longitudeTextView)
            val latitudeTextView = dialog.findViewById<TextView>(R.id.latitudeTextView)

            nameTextView.text = "You're Here!"
            longitudeTextView.text = "Longitude: $longitude"
            directionsButton.text = "Return to Map"
            latitudeTextView.text = "Latitude: $latitude"

            dialog.show()


            directionsButton.setOnClickListener{
              dialog.hide()
            }

            true // return true to indicate that the event has been consumed
        }

        //add marker to mapview
        mapView.overlays.add(marker)
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
        if (distanceString!!.toInt()==null){
            distance = 25
        }
        else {
            distance = distanceString!!.toInt()
        }



=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                    Log.e(ContentValues.TAG, "responses: " + ob.locName)
                    println(ob.locName)
                }
                Log.e(ContentValues.TAG, "responses: " + distance )
                println(distance)
=======
                    //Log.e(ContentValues.TAG, "responses: " + ob.locName)
                    //println(ob.locName)

                }

                val policy = ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                mapView =  view!!.findViewById(R.id.mvOne)

                mapView.setTileSource(TileSourceFactory.MAPNIK)
                mapView.mapCenter
                mapView.setMultiTouchControls(true)
                mapView.getLocalVisibleRect(Rect())
                mapController = mapView.controller


                //set the initial zoom
                mapController.setZoom(15.0)

                managePermissions()

                mapView.setMultiTouchControls(true)


                for (hotspot in hotspots) {
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(hotspot.lat, hotspot.lng)
                    marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_location_on_24, null)
                    marker.setOnMarkerClickListener { marker, mapView ->
                        val latitude = marker.position.latitude
                        val longitude = marker.position.longitude
                        val dialog = Dialog(requireContext())
                        dialog.setContentView(R.layout.custom)

                        val window = dialog.window
                        window?.setBackgroundDrawableResource(android.R.color.transparent)
                        window?.setGravity(Gravity.BOTTOM)
                        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                        val directionsButton = dialog.findViewById<Button>(R.id.directionsButton)

                        val nameTextView = dialog.findViewById<TextView>(R.id.nameTextView)
                        val longitudeTextView = dialog.findViewById<TextView>(R.id.longitudeTextView)
                        val latitudeTextView = dialog.findViewById<TextView>(R.id.latitudeTextView)

                        nameTextView.text = hotspot.locName
                        longitudeTextView.text = "Longitude: $longitude"
                        latitudeTextView.text = "Latitude: $latitude"

                        dialog.show()


                        directionsButton.setOnClickListener{
                            val startPoint = GeoPoint(-29.8393456,30.8427937)
                            val endPoint = GeoPoint(hotspot.lat,  hotspot.lng) //
                            getDirections(startPoint.latitude, startPoint.longitude, endPoint.latitude, endPoint.longitude)
                            dialog.hide()
                            // Get the list of route instructions
                            val routeInstructions = getRouteInstructions(startPoint.latitude, startPoint.longitude, endPoint.latitude, endPoint.longitude)

                            Log.d("routeInstructions","$routeInstructions")
                            // Display the route instructions in a CardView dialog
                            showRouteInstructionsDialog(routeInstructions, startPoint)
                        }

                        true // return true to indicate that the event has been consumed
                    }

                    mapView.overlays.add(marker)

                }

                mapView.invalidate()
>>>>>>> Stashed changes
            }
            override fun onFailure(call: Call<ArrayList<Hotspot>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.message.toString())
            }

        })

    }
}