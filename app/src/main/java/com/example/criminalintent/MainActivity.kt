package com.example.criminalintent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
CrimeListFragment.Callbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

    }

    override fun onCrimeSelected(crimeId: UUID) {
        Log.d(TAG, "MainActivity.onCrimeSelected: $crimeId")
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}





/*
private lateinit var tvInfo : TextView
private lateinit var etInfo : EditText
*/
/*
tvInfo = findViewById(R.id.tv_info)
etInfo = findViewById(R.id.et_info)


val titleWatcher = object : TextWatcher {
    override fun beforeTextChanged(
        sequence: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
        tvInfo.text = sequence.toString()
    }
    override fun onTextChanged(
        sequence: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        tvInfo.text = sequence.toString()
    }
    override fun afterTextChanged(sequence: Editable?) {
        // This one too
    }
}
etInfo.addTextChangedListener(titleWatcher)
 */