package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter : CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CrimeAdapter(crimeListViewModel.crimes)
        crimeRecyclerView.adapter = adapter

        return view

    }

    private inner class CrimeAdapter(var crimes: List<Crime>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].requiresPolice) 1 // seriousCrimeHolder
            else 0 // crimeHolder
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 0) {
                val viewCrime = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                return CrimeHolder(viewCrime)
            }

           val viewSeriousCrime = layoutInflater.inflate(R.layout.list_item_serious_crime, parent, false)
           return SeriousCrimeHolder(viewSeriousCrime)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder , position: Int) {
            val crime = crimes[position]
            if (crime.requiresPolice) {
                val seriousCrimeHolder : SeriousCrimeHolder = holder as SeriousCrimeHolder
                seriousCrimeHolder.bind(crime)

            }
            else {
                  val crimeHolder : CrimeHolder = holder as CrimeHolder
                  crimeHolder.bind(crime)
            }
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

    }

    private inner class CrimeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime : Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime : Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View?) {
           Log.i(TAG, crime.title)
        }

    }


    private inner class SeriousCrimeHolder(view : View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime : Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.serious_crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.serious_crime_date)
        private val reportButton : Button = itemView.findViewById(R.id.serious_crime_button)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime : Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()

            reportButton.setOnClickListener {
                Log.i("Click", "button")
            }

        }

        override fun onClick(v: View?) {
            Log.i(TAG, crime.title)
        }



    }


    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

}