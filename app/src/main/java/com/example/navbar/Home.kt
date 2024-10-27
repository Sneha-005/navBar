package com.example.navbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Home : Fragment() {

    private lateinit var usernameInput: EditText
    private lateinit var fetchButton: Button
    private lateinit var userNameTextView: TextView
    private lateinit var aboutTextView: TextView
    private lateinit var countryTextView: TextView

    private val sharedViewModel: SharedViewModel by activityViewModels() // Use SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        usernameInput = view.findViewById(R.id.usernameInput)
        fetchButton = view.findViewById(R.id.fetchButton)
        userNameTextView = view.findViewById(R.id.userNameTextView)
        aboutTextView = view.findViewById(R.id.aboutTextView)
        countryTextView = view.findViewById(R.id.countryTextView)

        // Set up the button click listener
        fetchButton.setOnClickListener {
            val username = usernameInput.text.toString()
            if (username.isNotEmpty()) {
                sharedViewModel.setUsername(username) // Set username in SharedViewModel
                apiCall(username)
            }
        }

        return view
    }

    private fun apiCall(userName: String) {
        lifecycleScope.launch {
            try {
                val userData = RetrofitInstance.api.getUserData(userName)
                updateUI(userData)
            } catch (e: Exception) {
                println("Error fetching user data: ${e.message}")
            }
        }
    }

    private fun updateUI(userData: userData) {
        userNameTextView.text = "User Name: ${userData.username ?: "N/A"}"
        aboutTextView.text = "About: ${userData.about ?: "N/A"}"
        countryTextView.text = "Country: ${userData.country ?: "N/A"}"
    }
}
