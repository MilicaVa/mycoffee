package com.example.mycoffee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoffee.databinding.RvCoffeeItemBinding
import com.example.mycoffee.models.Coffee
import com.example.mycoffee.HomeFragmentDirections
import androidx.navigation.Navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class RvCoffeeAdapterAdapter(private  val coffeesList : java.util.ArrayList<Coffee>) : RecyclerView.Adapter<RvCoffeeAdapter.ViewHolder>() {    class ViewHolder(val binding : RvCoffeeItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return  ViewHolder(RvCoffeeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return coffeesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = coffeesList[position]
        holder.apply {
            binding.apply {
                tvNameItem.text = currentItem.name
                tvPriceItem.text = currentItem.price
                tvIdItem.text = currentItem.id
                Picasso.get().load(currentItem.imgUri).into(imgItem)

                itemView.setOnClickListener {
                    currentItem.isSelected = !currentItem.isSelected
                    notifyItemChanged(position)
                }

                imgEdit.setOnClickListener {

                    val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(
                        currentItem.id.toString(),
                        currentItem.name.toString(),
                        currentItem.price.toString(),
                        currentItem.imgUri.toString()
                    )
                    findNavController(holder.itemView).navigate(action)
                }

                imgDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Delete item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes"){_,_ ->
                            val firebaseRef = FirebaseDatabase.getInstance().getReference("coffees")
                            val storageRef = FirebaseStorage.getInstance().getReference("Images")

                            storageRef.child(currentItem.id.toString()).delete()
                            // realtime database
                            firebaseRef.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(holder.itemView.context,"Item removed successfully" ,Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {error ->
                                    Toast.makeText(holder.itemView.context,"error ${error.message}" ,Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("No"){_,_ ->
                            Toast.makeText(holder.itemView.context,"canceled" ,Toast.LENGTH_SHORT).show()
                        }
                        .show()
                }
            }
        }
    }

}