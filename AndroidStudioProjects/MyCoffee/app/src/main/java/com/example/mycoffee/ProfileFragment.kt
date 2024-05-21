package com.example.mycoffee

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.mycoffee.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dohvatanje podataka iz SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "No Name") // "No Name" je default vrednost
        val surname = sharedPreferences.getString("surname", "No Surname")
        val email = sharedPreferences.getString("email", "No Email")


        // Postavljanje teksta na TextViews
        binding.txtName.text = name
        binding.txtSurname.text = surname
        binding.txtEmail.text = email

        binding.actionButton.setOnClickListener {
            // Kreiranje AlertDialog-a za izmenu informacija
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_edit_profile, null)

            val editName = view.findViewById<EditText>(R.id.editName)
            val editSurname = view.findViewById<EditText>(R.id.editSurname)

            // Postavite trenutne vrednosti
            editName.setText(binding.txtName.text)
            editSurname.setText(binding.txtSurname.text)

            AlertDialog.Builder(context)
                .setTitle("Edit Profile")
                .setView(view)
                .setPositiveButton("Save") { dialog, which ->
                    // Ažuriranje teksta TextView-ova na profilu
                    binding.txtName.text = editName.text.toString()
                    binding.txtSurname.text = editSurname.text.toString()

                    // Opciono: Sačuvajte promene u SharedPreferences
                    val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("name", editName.text.toString())
                    editor.putString("surname", editSurname.text.toString())
                    editor.apply()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }
    override fun onResume() {
        super.onResume()
        updateProfileData()
    }

    private fun updateProfileData() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "No Name") // "No Name" je default vrednost
        val surname = sharedPreferences.getString("surname", "No Surname")
        val email = sharedPreferences.getString("email", "No Email")

        // Postavljanje teksta na TextViews
        binding.txtName.text = name
        binding.txtSurname.text = surname
        binding.txtEmail.text = email
    }


}
