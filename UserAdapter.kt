package com.example.practiceapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.practiceapplication.AppUtils
import com.example.practiceapplication.Model.Item
import com.example.practiceapplication.R
import com.example.practiceapplication.databinding.UserShowBinding
import com.squareup.picasso.Picasso

class UserAdapter(var context: Context,  var userlist: MutableList<Item>):RecyclerView.Adapter<UserAdapter.MyviewHolder>() {

    var editclicklisners:((position:Int,user:Item)->Unit)?=null
    var deleteclicklisners:((position:Int,User:Item)->Unit)?=null
    class MyviewHolder(var binding:UserShowBinding) :RecyclerView.ViewHolder(binding.root){
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
      var binding= UserShowBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyviewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
       var user = userlist[position]
        holder.binding.name.text = user.name
        holder.binding.email.text=user.email
        holder.binding.gender.text=user.gender
//        if (!user.image.isNullOrEmpty()) {
////            Glide.with(context)
////                .load(user.image)
////                .diskCacheStrategy(DiskCacheStrategy.NONE)
////                .skipMemoryCache(true)
////                .into(holder.binding.image)
//            Picasso.get().load(user.image).into(holder.binding.image)
//        } else {
//            // If image URI is empty, load a placeholder or set a default image
//            holder.binding.image.setImageResource(R.drawable.baseline_add_a_photo_24)
//        }
        if(user.image!=null){
            holder.binding.image.setImageBitmap(AppUtils.getBitmapFromAbsolutePath(user.image))
//
        }

        holder.binding.edit.setOnClickListener {
            editclicklisners?.invoke(position,user)
        }
        holder.binding.delete.setOnClickListener {
            deleteclicklisners?.invoke(position,user)
        }

    }
    fun setUser(mutableList: MutableList<Item>){
        this.userlist= mutableList
        notifyDataSetChanged()
    }
    fun deleteUser(position: Int) {
        userlist.removeAt(position)
        notifyItemRemoved(position)
    }
}

