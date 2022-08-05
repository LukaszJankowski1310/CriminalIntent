package com.example.criminalintent

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File
import java.util.*

private const val ARG_FILE = "file"
private const val TAG = "ZoomPhotoFragment"

class ZoomPhotoFragment : DialogFragment() {

    private lateinit var zoomedPhoto : ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_zoom_photo, container, true)
        zoomedPhoto = view.findViewById(R.id.zoomed_photo)

        val photoFile = arguments?.getSerializable(ARG_FILE) as File

        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            zoomedPhoto.setImageBitmap(bitmap)
            Log.d(TAG, "file exists")
        } else {
            Log.d(TAG, "file does not exists")
            zoomedPhoto.setImageBitmap(null)
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return Dialog(requireContext())

    }


    companion object {
        fun newInstance(file: File) : DialogFragment {
            val args = Bundle().apply {
                putSerializable(ARG_FILE, file)
            }
            return ZoomPhotoFragment().apply {
                arguments = args
            }
        }
    }
}