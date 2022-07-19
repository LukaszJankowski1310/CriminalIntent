package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
  //  private var l : List<Crime> = listOf(Crime())
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter





        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
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
        private val solvedImageView : ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime : Crime) {
            this.crime = crime

            val crimeDate = DateFormat.format("dd MMMM yyyy  EEEE", this.crime.date)
            titleTextView.text = this.crime.title
            dateTextView.text = crimeDate.toString()


            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
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
            val crimeDate = DateFormat.format("dd MMMM yyyy  EEEE", this.crime.date)
            dateTextView.text = crimeDate.toString()

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