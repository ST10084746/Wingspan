package com.example.wingspan.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.wingspan.R
import com.google.firebase.firestore.FirebaseFirestore

class AddObservationFragment : Fragment() {

    private lateinit var btnUploadSighting: Button
    private lateinit var etSciName: EditText
    private lateinit var etComName : EditText
    private lateinit var etSpecCode : EditText
    private lateinit var db: FirebaseFirestore



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

        btnUploadSighting = view.findViewById(R.id.btn_uploadSighting)
        etComName = view.findViewById(R.id.et_comName)
        etSpecCode = view.findViewById(R.id.et_specCode)
        etSciName = view.findViewById(R.id.et_sciName)



        btnUploadSighting.setOnClickListener {
            var sighting : MutableMap<String, Any> = HashMap()
            sighting["speciesCode"] = etSpecCode.text.toString()
            sighting["sciName"] = etSciName.text.toString()
            sighting["comName"] = etComName.text.toString()

            db.collection("Observations")
                .add(sighting)
                .addOnSuccessListener {
                    Toast.makeText(context, "Observation Added", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }










        }
    }
}