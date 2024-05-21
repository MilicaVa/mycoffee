package com.example.mycoffee
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycoffee.adapter.RvCoffeeAdapterAdapter
import com.example.mycoffee.adapter.RvCoffeeAdapter
import com.example.mycoffee.databinding.FragmentHomeBinding
import com.example.mycoffee.models.Coffee
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private  val binding get() = _binding!!

    private lateinit var mycoffeesList: ArrayList<Coffees>
    private lateinit var  firebaseRef : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater , container, false)

        binding.btnAdd.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("coffees")
        coffeesList = arrayListOf()

        fetchData()

        binding.rvCoffees.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }

        return binding.root
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                coffeessList.clear()
                if (snapshot.exists()){
                    for (contactSnap in snapshot.children){
                        val coffees = contactSnap.getValue(Coffees::class.java)
                        coffeessList.add(coffees!!)
                    }
                }
                val rvAdapter = RvCoffeeAdapterAdapter(coffeesList)
                binding.rvCoffee.adapter = rvAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context," error : $error",Toast.LENGTH_SHORT).show()
            }

        })
    }
}