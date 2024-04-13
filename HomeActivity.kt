package com.example.practiceapplication.RoomDatabase

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practiceapplication.Adapter.UserAdapter
import com.example.practiceapplication.Model.Item
import com.example.practiceapplication.R
import com.example.practiceapplication.UserDatabase.Database
import com.example.practiceapplication.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var mAdapter:UserAdapter
    private var userlist= mutableListOf<Item>()
    var db:Database?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        db= Database.getInstence(this)

        mAdapter= UserAdapter(this, userlist)
        binding.recycle.layoutManager= LinearLayoutManager(this)
        binding.recycle.adapter=mAdapter

        mAdapter.editclicklisners={ position, user ->

                var intent = Intent(this, UserAddActivity::class.java)
                intent.putExtra("USER", user)
                startActivity(intent)



        }
        mAdapter.deleteclicklisners={ position, user ->
//            Database.getInstence(this)?.userDao()!!.deleteUser(User)
//           mAdapter.deleteUser(position)
           showDialog(user,position)
        }


    }

    private fun readUser(){
       userlist= Database.getInstence(this)?.userDao()!!.getAllUser()
        mAdapter.setUser(userlist)
    }

    override fun onResume() {
        super.onResume()
        readUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.user->{
                try {
                    var intent= Intent(this, UserAddActivity::class.java)
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }

                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
    private fun showDialog(user:Item, positin:Int){
        var builder= AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete this user?")
        builder.setPositiveButton("Delete",DialogInterface.OnClickListener{ dialog, which ->
        Database.getInstence(this)?.userDao()!!.deleteUser(user)
            Toast.makeText(this,"User Delated", Toast.LENGTH_SHORT).show()
        mAdapter.deleteUser(positin)

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which ->
            dialog.dismiss()
        })
        var dialog= builder.create()
        dialog.show()


    }
}


