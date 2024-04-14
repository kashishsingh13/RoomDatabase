package com.example.roomdatabasepratice.Registertion.Model

data class UserDetails(
    var users:String,
    var type: String
){
    override fun toString(): String {
        return users
    }
}
