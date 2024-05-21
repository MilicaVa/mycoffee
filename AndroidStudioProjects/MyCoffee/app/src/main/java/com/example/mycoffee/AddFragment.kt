package com.example.mycoffee

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.mycoffee.databinding.FragmentAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.mycoffee.models.Coffee
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private  val binding get() = _binding!!

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater , container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("coffees")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        binding.btnSend.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.imgAdd.setImageURI(it)
            if (it != null){
                uri = it
            }
        }

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        return binding.root
    }

    private fun saveData() {
        val name = binding.edtName.text.toString()
        val price = binding.edtPrice.text.toString()

        if (name.isEmpty()) binding.edtName.error = "write a name"
        if (price.isEmpty()) binding.edtPrice.error = "write a price"

        val coffeeId = firebaseRef.push().key!!
        var coffees : Coffee

        uri?.let{
            storageRef.child(coffeeId).putFile(it)
                .addOnSuccessListener { task->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            Toast.makeText(context, " Image stored successfully",Toast.LENGTH_SHORT).show()
                            val imgUrl = url.toString()

                            coffees = Coffees(coffeeId,name , price , imgUrl)

                            firebaseRef.child(coffeeId).setValue(coffees)
                                .addOnCompleteListener{
                                    Toast.makeText(context, " data stored successfully",Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{error ->
                                    Toast.makeText(context, "error ${error.message}",Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }

    }

}