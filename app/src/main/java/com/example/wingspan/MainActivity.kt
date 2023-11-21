package com.example.wingspan

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

lateinit var countDownTimer: CountDownTimer



class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.bottom_nav)



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mapFragment, R.id.addObservationFragment2, R.id.observationFragment2,R.id.infoFragment2, R.id.settingsFragment2)
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView

        toolbar.setTitle("Map")

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()|| super.onSupportNavigateUp()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        MenuCompat.setGroupDividerEnabled(menu!!, true)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_logout) {
            showLogoutConfirmationDialog() // Call the method to display the dialog
            return true
        } else if (id == R.id.action_delete) {
            accountDeletionRequest()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out? You will have to login again to use the app.")
        builder.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int -> performLogout() }
        builder.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun accountDeletionRequest() {
        val url = "https://forms.gle/6JbGHRzbg8Un3rA76"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }




    private fun attemptRequest() {
        countDownTimer = object : CountDownTimer(5*1000,1000){
            override fun onTick(p0: Long) {
                Log.i("MainActivity", "Trying again in ${p0/1000} seconds")
            }

            override fun onFinish() {
                //getSightings()
                countDownTimer.cancel()
            }

        }
        countDownTimer.start()
    }

}

