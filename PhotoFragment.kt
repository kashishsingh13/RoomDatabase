package com.example.photosapplication

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.photosapplication.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var rs:Cursor

    companion object {
        private const val REQUEST_PERMISSION_CODE = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE)
        }
        else {
            loadImages()
        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // Permission granted, proceed to load images
            loadImages()

        }

    }

    private fun loadImages() {
        try {
            val cols = arrayOf(MediaStore.Images.Media.DATA)
            rs = requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cols,
                null,
                null,
                null
            )!!

            binding.grid.adapter = ImageAdapter(requireContext(), rs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    inner class ImageAdapter(private val context: Context, private val cursor: Cursor):BaseAdapter(){
        override fun getCount(): Int {
            return rs.count
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var iv = ImageView(context)
            rs.moveToPosition(position)
            var path =rs.getString(0)
            var bitmap= BitmapFactory.decodeFile(path)
            iv.setImageBitmap(bitmap)
            iv.layoutParams=AbsListView.LayoutParams(300,300)
            return iv

        }

    }


}
