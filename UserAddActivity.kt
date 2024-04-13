package com.example.practiceapplication.RoomDatabase

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.practiceapplication.AppUtils
import com.example.practiceapplication.Model.Item
import com.example.practiceapplication.R
import com.example.practiceapplication.UserDatabase.Database
import com.example.practiceapplication.databinding.ActivityUserAddBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import kotlin.concurrent.thread

class UserAddActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserAddBinding
    private var user:Item?=null
    var  db : Database? = null
    private var imageUri:Uri?=null
    private var imageBitmap: Bitmap? = null
    private var imagePath = ""

    private var cameraContract= registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it){
            binding.image.setImageURI(imageUri)
        }
        else{
            imageUri=null
        }

    }

    private var galleryContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            // selected
//            binding.ivThumbnail.setImageURI(it)
            imagePath=""
            imageBitmap = getBitmapFromUri(it)
            imageBitmap?.let { bitmap ->
                binding.image.setImageBitmap(bitmap)
            }

        } else {
            Log.d("PICK_MEDIA", ": no media selected")
        }
    }

    private fun getBitmapFromUri(uri:Uri):Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        }catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= Database.getInstence(this)

      user=if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
         intent.getParcelableExtra("USER", Item::class.java)

      }else{
          intent.getParcelableExtra("USER")
      }
        if(user!=null){
            binding.submit.text="Update"
            binding.name.setText(user!!.name)
            binding.email.setText(user!!.email)
//
                when(user!!.gender){
                    "Male"->binding.radio.check(R.id.male)
                    "Female"->binding.radio.check(R.id.female)
                }
//            if (!user!!.image.isNullOrEmpty()) {
//
//                Picasso.get().load(user!!.image).into(binding.image)
//            }

            imageBitmap =AppUtils.getBitmapFromAbsolutePath(user!!.image)
            imagePath = user!!.image
            binding.image.setImageBitmap(imageBitmap)

        }

        binding.image.setOnClickListener {

            showcustomDialog()
        }


        binding.submit.setOnClickListener {
            var name = binding.name.text.toString().trim()
            var email = binding.email.text.toString().trim()
            var gender= when(binding.radio.checkedRadioButtonId){
                R.id.male->"Male"
                R.id.female->"Female"
                else -> ""
            }


            updaterecord(name, email, gender)
        }
    }

    private fun updaterecord(name: String, email: String, gender: String) {
        var message = ""
        thread(start = true) {
            var userObject = Item(
                name = name, email = email, gender = gender,
                id = if (user != null) user!!.id else 0,
                createAt = if (user != null) user!!.createAt else System.currentTimeMillis()

            )
            if (imageUri != null) {
                // Camera image selected
                val imagePath = saveImageFromUri(imageUri!!)
                userObject.image = imagePath ?: ""
            } else if (imageBitmap != null) {
                // Gallery image selected
                userObject.image = AppUtils.getImagePathFromBitmap(this, imageBitmap,imagePath) ?:""
            }
                if (user != null) {
                    db!!.userDao().updateUser(userObject)
                    message = "Record updated successfully"

                } else {
                    db!!.userDao().insetUser(userObject)
                    message = "Record added successfully"
                }


                runOnUiThread {
                    Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
                    onBackPressedDispatcher.onBackPressed()

                }
            }


    }
    private fun createimageUri():Uri?{
        var fileName= "${System.currentTimeMillis()}.jpg"
        var imagefile= File(filesDir,fileName)
        return FileProvider.getUriForFile(this,"com.example.practiceapplication.file_provider",
        imagefile)

    }
    private  fun showcustomDialog(){
        var view = layoutInflater.inflate(R.layout.custom_layout, null)
        var builder= AlertDialog.Builder(this).setView(view)
        var dialog= builder.create()
        dialog.show()
        var btncamera= view.findViewById<LinearLayout>(R.id.camera)
        var btngallery= view.findViewById<LinearLayout>(R.id.gallery)

        btncamera.setOnClickListener {
            dialog.dismiss()
            imageUri = createimageUri()
          cameraContract.launch(imageUri)
        }


        btngallery.setOnClickListener {
            dialog.dismiss()
            galleryContract.launch("image/*")
        }
    }
    private fun saveImageFromUri(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}


//class RegisterActivity : AppCompatActivity()  {
//    private lateinit var binding: ActivityRegisterBinding
//    private var register: Register? = null
//    var db: RegisterDatabase? = null
//    private var selectedDate: String = ""
//    private var selectedTime: String = ""
//    private var previousSelectedDate: String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        db = RegisterDatabase.getInstance(this)
//        register = intent.getParcelableExtra("User",Register::class.java)
//        if (register!=null) {
//            binding.register.text= "UPDATE"
//            binding.name.setText(register!!.name)
//            binding.email.setText(register!!.email)
//            binding.number.setText(register!!.mobile)
//            binding.password.setText(register!!.password)
//            binding.cpassword.setText(register!!.cpassword)
//            selectedDate = register!!.date
//            selectedTime = register!!.time
//            binding.date.setText(selectedDate)
//            binding.time.setText(selectedTime)
//        }
////        val sharedPreferences = getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)
////        previousSelectedDate = sharedPreferences.getString("selectedDate", "") ?: ""
//        binding.login.setOnClickListener {
//            var intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        binding.date.setOnClickListener {
//            val c = Calendar.getInstance()
//
//            val year = c.get(Calendar.YEAR)
//            val month = c.get(Calendar.MONTH)
//            val day = c.get(Calendar.DAY_OF_MONTH)
//
//            val datePickerDialog = DatePickerDialog(
//                // on below line we are passing context.
//                this,
//                { view, year, monthOfYear, dayOfMonth ->
//                    selectedDate = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
//                    binding.date.setText(selectedDate)
//                    previousSelectedDate = selectedDate
//                    val sharedPreferences = getSharedPreferences("RegisterPrefs", MODE_PRIVATE)
//                    sharedPreferences.edit().putString("selectedDate", selectedDate).apply()
//                },
//                year,
//                month,
//                day
//            )
//            if (selectedDate.isNotEmpty()) {
//                val selectedDateParts = selectedDate.split("-")
//                datePickerDialog.updateDate(
//                    selectedDateParts[2].toInt(), // year
//                    selectedDateParts[1].toInt() - 1, // month (Calendar.MONTH is zero-based)
//                    selectedDateParts[0].toInt() // day
//                )
//            }
//
//
//            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.DATE, 0) // Add 0 days to get today's date
//            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
//            datePickerDialog.show()
//        }
//        binding.time.setOnClickListener {
//            val c = Calendar.getInstance()
//
//            // on below line we are getting our hour, minute.
//            val hour = c.get(Calendar.HOUR_OF_DAY)
//            val minute = c.get(Calendar.MINUTE)
//
//            // on below line we are initializing
//            // our Time Picker Dialog
//            val timePickerDialog = TimePickerDialog(
//                this,
//                { view, hourOfDay, minute ,->
//                    // on below line we are setting selected
//                    // time in our text view.
//                    selectedTime = "$hourOfDay:$minute"
//                    binding.time.setText(selectedTime)
////                    binding.time.setText("$hourOfDay:$minute")
//                },
//                hour,
//                minute,
//                false
//            )
//            // at last we are calling show to
//            // display our time picker dialog.
//            timePickerDialog.show()
//
//
//
//        }
//        binding.register.setOnClickListener {
//            var name = binding.name.text.toString().trim()
//            var email = binding.email.text.toString().trim()
//            var number = binding.number.text.toString().trim()
//            var password = binding.password.text.toString().trim()
//            var cpassword = binding.cpassword.text.toString().trim()
//            var music = (binding.music.isChecked)
//            var read = (binding.read.isChecked)
//            if(!RegisterPage.isvalidname(name)){
//                binding.name.error="Invalid name"
//            }
//
//            else if (!RegisterPage.isValidEmail(email)) {
//                Showerror(binding.email, "Invalid Email")
//            } else if (!RegisterPage.isValidNumber(number)) {
//                Showerror(binding.number, "Invalid Mobile Number ")
//            } else if (!RegisterPage.isValidpassword(password)) {
//                Showerror(binding.password, "Invalid Password ")
//            } else if (cpassword != password) {
//                Showerror(binding.cpassword, "Password not Match")
//            }
//
//            else {
//                Updaterecord(name, email, number, password, cpassword,music.toString(),read.toString(),selectedDate,selectedDate)
//            }
//        }
//    }
//    private fun Showerror(edittext: EditText, message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//    private fun Updaterecord(
//        name: String,
//        email: String,
//        number: String,
//        password: String,
//        cpassword: String,
//        music: String,
//        read: String,
//        selectedDate: String,
//        selectedDate1: String
//    ) {
//        var hobbie=""
//        if (music.toBoolean()){
//            hobbie += "Music,"
//        }
//        if (read.toBoolean()){
//            hobbie += "Read, "
//        }
//        var message=""
//        thread(start = true) {
//            var userobject = Register(name=name, email = email, mobile = number, password = password, cpassword = cpassword,music=music,read=read,hobbie=hobbie,
//                date = selectedDate, time = selectedTime,
//                id = if (register!=null) register!!.id else 0)
//
//            if (register!=null){
//                db!!.registerdao().updateUser(userobject)
//                message="Update user Data"
//            }
//            else {
//                db!!.registerdao().enterUser(userobject)
//                message = "Register Succefully"
//            }
//
//            runOnUiThread {
//                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
//                var intent = Intent(this, HomeActivity::class.java)
//                startActivity(intent)
//                finish()
////               onBackPressedDispatcher.onBackPressed()
//            }
//        }
//
//
//    }
//
//
//
//}

