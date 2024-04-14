package com.example.roomdatabasepratice.Registertion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabasepratice.R
import com.example.roomdatabasepratice.Registertion.Adapter.LoginAdapter
import com.example.roomdatabasepratice.Registertion.Database.RegisterDatabase
import com.example.roomdatabasepratice.Registertion.Model.Register
import com.example.roomdatabasepratice.Registertion.Model.UserDetails
import com.example.roomdatabasepratice.databinding.ActivityHomeBinding
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var registerAdapter: LoginAdapter
    private var userList = mutableListOf<Register>()
    private var db: RegisterDatabase? = null
    private var selectedUserType: String ?=null
    private var usersList = mutableListOf<UserDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = RegisterDatabase.getInstance(this)

        prepareState()
        val stateAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            usersList.map { it.users }
        )
        binding.users.adapter = stateAdapter

        binding.users.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedUserType = usersList[position].type
                when (selectedUserType) {
                    "Name" -> {
                        binding.nameserchview1.visibility = View.VISIBLE
                        binding.nameserchview.visibility = View.GONE
                        binding.nameserchview2.visibility = View.GONE
                        binding.nameserchview3.visibility = View.GONE
                        filterDataName()
                    }
                    "All" -> {
                        binding.nameserchview.visibility = View.VISIBLE
                        binding.nameserchview1.visibility = View.GONE
                        binding.nameserchview2.visibility = View.GONE
                        binding.nameserchview3.visibility = View.GONE
                        filterData()
                    }
                    "Email" -> {
                        binding.nameserchview2.visibility = View.VISIBLE
                        binding.nameserchview.visibility = View.GONE
                        binding.nameserchview1.visibility = View.GONE
                        binding.nameserchview3.visibility = View.GONE
                        filterDataEmail()
                    }
                    "Phone" -> {
                        binding.nameserchview3.visibility = View.VISIBLE
                        binding.nameserchview.visibility = View.GONE
                        binding.nameserchview1.visibility = View.GONE
                        binding.nameserchview2.visibility = View.GONE
                        filterDataPhone()
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        registerAdapter = LoginAdapter(this, userList)
        binding.recycle.adapter = registerAdapter
        binding.recycle.layoutManager = LinearLayoutManager(this)

        binding.nameserchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData()
                return true
            }
        })

        binding.nameserchview1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDataName()
                return true
            }
        })


        binding.nameserchview2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterDataEmail()
                return true
            }
        })

        binding.nameserchview3.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDataPhone()
                return true
            }
        })

    }

private fun filterData() {
    val filteredList = userList.filter { user ->
        (selectedUserType == "All" || user.type == selectedUserType) &&
                (user.name.contains(binding.nameserchview.query, ignoreCase = true) ||
                        user.email.contains(binding.nameserchview.query, ignoreCase = true) ||
                        user.mobile.contains(binding.nameserchview.query, ignoreCase = true))
    }
    registerAdapter.setuser(filteredList.toMutableList())
}


private fun filterDataName() {
    val filteredList = userList.filter { user ->
        (selectedUserType == "Name" || user.type == selectedUserType) &&
                (user.name.contains(binding.nameserchview1.query, ignoreCase = true) )
    }
    registerAdapter.setuser(filteredList.toMutableList())
}

private fun filterDataEmail() {
    val filteredList = userList.filter { user ->
        (selectedUserType == "Email" || user.type == selectedUserType) &&
                (user.email.contains(binding.nameserchview2.query, ignoreCase = true) )
    }
    registerAdapter.setuser(filteredList.toMutableList())
}

private fun filterDataPhone() {
    val filteredList = userList.filter { user ->
        (selectedUserType == "Phone" || user.type == selectedUserType) &&
                (user.mobile.contains(binding.nameserchview3.query, ignoreCase = true) )
    }
    registerAdapter.setuser(filteredList.toMutableList())
}

    private fun readUser() {
        userList.clear()
        userList.addAll(db?.registerdao()?.getuser() ?: emptyList())
        filterData()
        filterDataName()
        filterDataEmail()
        filterDataPhone()
    }

    private fun prepareState() {
        usersList.add(UserDetails("All","All"))
        usersList.add(UserDetails("Name","Name"))
        usersList.add(UserDetails("Email","Email"))
        usersList.add(UserDetails("Phone","Phone"))
    }

    override fun onResume() {
        super.onResume()
        readUser()
    }

}




///    private fun filterDataPhone(searchText: String = "") {
//        val filteredList = if (searchText.isNotEmpty()) {
//            userList.filter { user ->
//                (selectedUserType == "Phone" || user.type == selectedUserType) &&
//                        (user.mobile.contains(searchText, true))
//            }
//        } else {
//            userList.filter { user ->
//                selectedUserType == "Phone" || user.type == selectedUserType
//            }
//        }
//        registerAdapter.setuser(filteredList.toMutableList())
//    }

//private fun filterDataPhone(searchText: String = "") {
//        val filteredList = if (searchText.isNotEmpty()) {
//            userList.filter { user ->
//                (selectedUserType == "Phone" || user.type == selectedUserType) &&
//                        (user.mobile.contains(searchText, true))
//            }
//        } else {
//            userList.filter { user ->
//                selectedUserType == "Phone" || user.type == selectedUserType
//            }
//        }
//        registerAdapter.setuser(filteredList.toMutableList())
//    }


//private fun filterDataEmail(searchText: String = "") {
//        val filteredList = if (searchText.isNotEmpty()) {
//            userList.filter { user ->
//                (selectedUserType == "Email" || user.type == selectedUserType) &&
//                        (user.email.contains(searchText, true))
//            }
//        } else {
//            userList.filter { user ->
//                selectedUserType == "Email" || user.type == selectedUserType
//            }
//        }
//        registerAdapter.setuser(filteredList.toMutableList())
//    }

//private fun filterDataName(searchText: String = "") {
//        val filteredList = if (searchText.isNotEmpty()) {
//            userList.filter { user ->
//                (selectedUserType == "Name" || user.type == selectedUserType) &&
//                        (user.name.contains(searchText, true))
//            }
//        } else {
//            userList.filter { user ->
//                selectedUserType == "Name" || user.type == selectedUserType
//            }
//        }
//        registerAdapter.setuser(filteredList.toMutableList())
//    }

//    private fun filterData(searchText: String = "") {
//        val filteredList = if (searchText.isNotEmpty()) {
//            userList.filter { user ->
//                (selectedUserType == "All" || user.type == selectedUserType) &&
//                        (user.name.contains(searchText, true) ||
//                                user.email.contains(searchText, true) ||
//                                user.mobile.contains(searchText, true))
//            }
//        } else {
//            userList.filter { user ->
//                selectedUserType == "All" || user.type == selectedUserType
//            }
//        }
//        registerAdapter.setuser(filteredList.toMutableList())
//    }
