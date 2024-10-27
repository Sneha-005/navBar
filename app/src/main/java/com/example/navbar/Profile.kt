package com.example.navbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.navbar.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels() // Use SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Intercept the back press in this fragment
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeFragment, false) // Set homeFragment as the root
                    .build()

                findNavController().navigate(R.id.homeFragment, null, navOptions)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Observe the username from SharedViewModel
        sharedViewModel.username.observe(viewLifecycleOwner, Observer { username ->
            fetchUserData(username)
        })

        return binding.root
    }

    private fun fetchUserData(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the API to get user data
                val userData = RetrofitInstance.api.getUserData(username)

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    displayUserData(userData)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error fetching user data: ${e.message}")
                }
            }
        }
    }

    private fun displayUserData(userData: userData) {
        binding.nameTextView.text = "Name: ${userData.name ?: "N/A"}"
        binding.aboutTextView.text = "About: ${userData.about ?: "N/A"}"
        binding.countryTextView.text = "Country: ${userData.country ?: "N/A"}"
        binding.companyTextView.text = "Company: ${userData.company ?: "N/A"}"
        binding.rankingTextView.text = "Ranking: ${userData.ranking ?: "N/A"}"
        binding.reputationTextView.text = "Reputation: ${userData.reputation ?: "N/A"}"
        binding.schoolTextView.text = "School: ${userData.school ?: "N/A"}"
        binding.twitterTextView.text = "Twitter: ${userData.twitter ?: "N/A"}"
        binding.githubTextView.text = "GitHub: ${userData.gitHub ?: "N/A"}"
        binding.linkedInTextView.text = "LinkedIn: ${userData.linkedIN ?: "N/A"}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }
}
