package com.example.navbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.navbar.databinding.FragmentBadgesBinding
import com.example.navbar.databinding.FragmentProfileBinding
import com.example.navbar.databinding.FragmentSolvedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Badges : Fragment() {

    private var _binding: FragmentBadgesBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels() // Use SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Intercept the back press in this fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
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
        _binding = FragmentBadgesBinding.inflate(inflater, container, false)

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
                val userBadges = RetrofitInstance.api.getUserBadges(username)

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    displayUserBadges(userBadges)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error fetching user data: ${e.message}")
                }
            }
        }
    }

    private fun displayUserBadges(userBadges: userBadges) {
        binding.badgesCountTextView.text = "Badges Count : ${userBadges.badgesCount ?: "N/A"}"
        binding.activeBadgeTextView.text = "Active Badge : ${userBadges.activeBadge ?: "N/A"}"
        binding.upcomingBadgesContainer.removeAllViews()
        userBadges.upcomingBadges?.forEach { badge ->
            // Inflate the badge item layout
            val badgeView = LayoutInflater.from(requireContext())
                .inflate(R.layout.badge_item, binding.upcomingBadgesContainer, false)

            val badgeNameTextView: TextView = badgeView.findViewById(R.id.badgeNameTextView)

            badgeNameTextView.text = badge.name ?: "Unnamed Badge"
            binding.upcomingBadgesContainer.addView(badgeView)
        }
    };

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }
}
