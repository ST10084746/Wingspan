package com.example.wingspan.Fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.wingspan.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}