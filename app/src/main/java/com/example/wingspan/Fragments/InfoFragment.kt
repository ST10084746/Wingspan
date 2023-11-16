package com.example.wingspan.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wingspan.EBirdApiService
import com.example.wingspan.InfoAdapter
import com.example.wingspan.Models.BirdInfoItem
import com.example.wingspan.Models.Sighting
import com.example.wingspan.R
import com.example.wingspan.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment(), InfoAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var information: ArrayList<BirdInfoItem>
    private lateinit var infoAdapter: InfoAdapter
    private lateinit var retroInfoClient: Retro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        information = arrayListOf()
        recyclerView = view.findViewById(R.id.infoRecView);
        recyclerView.layoutManager = LinearLayoutManager(context)

        infoAdapter = InfoAdapter(information, this);
        recyclerView.adapter = infoAdapter;
        recyclerView


        loadInfo()


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onItemClick(position: Int) {
        val action = InfoFragmentDirections.actionInfoFragment2ToTaxonomyFragment(information[position])
        findNavController().navigate(action)

    }

    private  fun  addToList(info: BirdInfoItem){
        information.add(info)
    }

    private fun loadInfo(){
        retroInfoClient = Retro();
        val fmt = "json";

        val api = retroInfoClient.retrofit.create(EBirdApiService::class.java)
        val call = api.getInfo(fmt)

        call.enqueue(object : Callback<ArrayList<BirdInfoItem>>{
            override fun onResponse(
                call: Call<ArrayList<BirdInfoItem>>,
                response: Response<ArrayList<BirdInfoItem>>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure")
                }
                response.body()!!.forEach{ info->
                    addToList(info)
                }
                infoAdapter.notifyDataSetChanged()





            }

            override fun onFailure(call: Call<ArrayList<BirdInfoItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.message.toString())
            }

        })

    }

}