package com.example.wingspan.Fragments

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.location.GpsStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapthree.RouteInstructionsAdapter
import com.example.wingspan.EBirdApiService
import com.example.wingspan.Models.Hotspot
import com.example.wingspan.R
import com.example.wingspan.Retro
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment(), IMyLocationProvider, MapListener, GpsStatus.Listener {

    private val LOCATION_REQUEST_CODE = 0;

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener


    //init binding

    //mapview and map controller
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    private lateinit var myLocationOverLay: MyLocationNewOverlay
    private lateinit var controller: IMapController

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


        val view = inflater.inflate(R.layout.fragment_map, container, false)
        Configuration.getInstance().load(
            requireContext(),
            requireContext().getSharedPreferences("osmdroid", 0)
        )


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
        val startPoint = GeoPoint(geoPoint.latitude, geoPoint.longitude)
        mapController.setCenter(startPoint)
        mapController.setZoom(18.0)

        //create marker

        val marker = Marker(mapView)
        marker.position = startPoint

        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_location_on_24, null)

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

            val directionsButton = dialog.findViewById<Button>(R.id.directionsButton)
            val longitudeTextView = dialog.findViewById<TextView>(R.id.longitudeTextView)
            val latitudeTextView = dialog.findViewById<TextView>(R.id.latitudeTextView)

            longitudeTextView.text = "Test Longitude: $longitude"
            latitudeTextView.text = "Test Latitude: $latitude"

            dialog.show()


            directionsButton.setOnClickListener{

                val startPoint = GeoPoint(startPoint) //
                val endPoint = GeoPoint(-29.9300,  31.1000) //
                getDirections(startPoint.latitude, startPoint.longitude, endPoint.latitude, endPoint.longitude)
                dialog.hide()
                // Get the list of route instructions
                val routeInstructions = getRouteInstructions(startPoint.latitude, startPoint.longitude, endPoint.latitude, endPoint.longitude)

                Log.d("routeInstructions","$routeInstructions")
                // Display the route instructions in a CardView dialog
                showRouteInstructionsDialog(routeInstructions)
            }

            true // return true to indicate that the event has been consumed
        }

        //add marker to mapview
        mapView.overlays.add(marker)

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

    private fun showRouteInstructionsDialog(routeInstructions: ArrayList<String>) {
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
            // You can start the navigation here
            dialog.dismiss() // Close the dialog
        }
        // Show the dialog
        dialog.show()
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

        val distance = prefs.getInt("range", 25)
        val switch = prefs.getBoolean("unit", false)

        var multiplier = 0.0


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
        val distance = 25
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
            }

            override fun onFailure(call: Call<ArrayList<Hotspot>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.message.toString())
            }

        })



    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {

        TODO("Not yet implemented")
    }

    override fun stopLocationProvider() {
        TODO("Not yet implemented")
    }

    override fun getLastKnownLocation(): Location {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onGpsStatusChanged(event: Int) {
        TODO("Not yet implemented")
    }

    private fun isLocationPermissionGranted(): Boolean{
        val finelocation = ActivityCompat.checkSelfPermission(requireContext(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val courselocation = ActivityCompat.checkSelfPermission(requireContext(),
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finelocation && courselocation
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_REQUEST_CODE){
            if(grantResults.isNotEmpty()){
                for(result in grantResults){
                    Toast.makeText(requireContext(), "Permissions Granted", Toast.LENGTH_SHORT).show()
                    if(result == PackageManager.PERMISSION_GRANTED){
                        //Handle permission granted
                        //you coiuld re-initialize the map here if needed
                        //setupMap()
                    }
                    else{
                        Toast.makeText(requireContext(), "Permissions Denied", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    } //me



    private fun managePermissions(){
        val requestPermissions = mutableListOf<String>()
        if(!isLocationPermissionGranted()){
            //if these weren't granted

            requestPermissions.add(ACCESS_FINE_LOCATION)
            requestPermissions.add(ACCESS_COARSE_LOCATION)
        }
        if(requestPermissions.isNotEmpty()){

        }
    }
}