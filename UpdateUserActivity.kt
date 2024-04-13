package com.example.practiceapplication.RoomDatabase

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.practiceapplication.Model.Item
import com.example.practiceapplication.R
import com.example.practiceapplication.UserDatabase.Database
import com.example.practiceapplication.databinding.ActivityUpdateUserBinding

class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateUserBinding
    var db: Database? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= Database.getInstence(this)

        var user=intent.getParcelableExtra("USER", Item::class.java)
        var message = ""
        if(user!=null){
            binding.name.setText(user.name)
            binding.email.setText(user.email)
//           when(user.gender){
//               "MALE"->binding.radio.check(R.id.male)
//               "FEMALE"->binding.radio.check(R.id.female)
//
//           }

            binding.update.setOnClickListener {
            val updatedUser = Item(
                user.id,  // Assuming id is the primary key
                binding.name.text.toString(),
                binding.email.text.toString(),
                when(binding.radio.checkedRadioButtonId){
                    R.id.male->"MALE"
                    R.id.female->"FEMALE"
                    else->""
                }
            )
                db!!.userDao().updateUser(updatedUser)
                message = "Record updated successfully"
                finish()

            }
            binding.update.post {
                when (user.gender) {
                    "MALE" -> binding.radio.check(R.id.male)
                    "FEMALE" -> binding.radio.check(R.id.female)
                }
            }
        }


    }
}