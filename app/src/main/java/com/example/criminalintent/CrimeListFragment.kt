package com.example.criminalintent

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import android.view.*
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.security.auth.callback.Callback

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter = CrimeAdapter()

    private lateinit var emptyListInfoLayout: LinearLayout
    private lateinit var newCrimeButton: Button


    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks : Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        emptyListInfoLayout = view.findViewById(R.id.empty_list_info_text_view)
        newCrimeButton = view.findViewById(R.id.new_crime_button)

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

    override fun onStart() {
        super.onStart()
        newCrimeButton.setOnClickListener {
            addCrime()
        }
    }


    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                addCrime()
                true
            }
            else ->  return super.onOptionsItemSelected(item)
        }
    }


    private fun updateUI(crimes: List<Crime>) {
        adapter.setData(crimes)
        emptyListInfoLayout.visibility = if (crimes.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun addCrime() {
        val crime = Crime()
        crimeListViewModel.addCrime(crime)
        callbacks?.onCrimeSelected(crime.id)
    }

    private inner class CrimeAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

         var crimes : List<Crime> = listOf()

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

        fun setData(newCrimesList : List<Crime>) {
            val diffUtil = CrimeListDiffUtil(crimes, newCrimesList)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            crimes = newCrimesList
            diffResult.dispatchUpdatesTo(this)

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

            val crimeDate = DateFormat.format("dd MMMM yyyy  EEEE  HH:mm", this.crime.date)
            titleTextView.text = this.crime.title
            dateTextView.text = crimeDate.toString()


            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {
           callbacks?.onCrimeSelected(crime.id)
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