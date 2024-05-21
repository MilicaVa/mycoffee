package com.example.mycoffee

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mycoffee.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration // Članska varijabla
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = binding.bottomNav
        bottomNav.setupWithNavController(navController)

        // Konfiguracija AppBar-a da uključi samo određene fragmente kao top-level destinacije
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.videoFragment))
        // ID-evi "top level" destinacija
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Sakrij BottomNavigationView na SignInFragment i SignUpFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment, R.id.signUpFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    supportActionBar?.hide()
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment, R.id.signUpFragment -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.videoFragment -> {
                    navController.navigate(R.id.videoFragment)
                    true
                }
                R.id.navigation_overview -> {
                    showLogoutDialog()
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun clearUserData() {
        val sharedPreferences =  getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Ovo briše sve podatke iz SharedPreferences
        editor.apply()  // Primena promena
        println(editor)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                // Pretpostavimo da koristite FirebaseAuth za izlogovanje
                FirebaseAuth.getInstance().signOut()
                clearUserData()

                // Nakon izlogovanja, navigirajte nazad na ekran za prijavu
                navController.navigate(R.id.signInFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }


}
