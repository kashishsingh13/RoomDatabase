package com.example.practiceapplication.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "user_details")
@Parcelize
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id :Int=0,
    var name:String,
    var email:String,
    var gender:String,
    var image:String="",
    var createAt:Long=System.currentTimeMillis()
):Parcelable
