package com.example.luisfilho.listnativecontacts

import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by luis.filho on 14/08/2018.
 */
class MyAdapter(var myDataset: ArrayList<ContactInformation>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtId.text = myDataset[position].id
        holder.txtName.text = myDataset[position].name
        //holder.imgUser.setImageBitmap(myDataset[position].imgUser)
        Glide.with(holder.itemView.context).asBitmap().
                load(myDataset[position].imgUser).
                apply(RequestOptions().fitCenter().centerCrop().override(150, 150)).
                into(holder.imgUser)
        holder.txtPhone.text = myDataset[position].phone
        holder.itemView.setOnClickListener{
          //Perform Click
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtId: TextView = itemView.findViewById<TextView>(R.id.txtID)
        val txtName: TextView = itemView.findViewById<TextView>(R.id.txtName)
        val imgUser: ImageView = itemView.findViewById<CircleImageView>(R.id.imgUser)
        val txtPhone: TextView = itemView.findViewById<TextView>(R.id.txtPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_text_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    fun updateRecycle(listContacts : ArrayList<ContactInformation>){
        myDataset = ArrayList<ContactInformation>()
        myDataset.addAll(listContacts)
        notifyDataSetChanged()
    }


}
