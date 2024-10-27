package com.example.navbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.navbar.databinding.FragmentProfileBinding
import com.example.navbar.databinding.FragmentSolvedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Solved : Fragment() {

    private var _binding: FragmentSolvedBinding? = null
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
        _binding = FragmentSolvedBinding.inflate(inflater, container, false)

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
                val userSolved = RetrofitInstance.api.getUserSolved(username)

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    displayUserSolved(userSolved)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error fetching user data: ${e.message}")
                }
            }
        }
    }

    private fun displayUserSolved(userSolved: userSolved) {

        binding.solvedProblemCountTextView.text = "Total Solved Problems: ${userSolved.solvedProblem ?: "N/A"}"
        binding.easySolvedTextView.text = "Easy Problems Solved: ${userSolved.easySolved ?: "N/A"}"
        binding.mediumSolvedTextView.text = "Medium Problems Solved: ${userSolved.mediumSolved ?: "N/A"}"
        binding.hardSolvedTextView.text = "Hard Problems Solved: ${userSolved.hardSolved ?: "N/A"}"

        binding.submissionNumContainer.removeAllViews()

        userSolved.acSubmissionNum?.forEach { solved ->
            // Inflate the badge item layout
            val solvedView = LayoutInflater.from(requireContext())
                .inflate(R.layout.acsubmission_item, binding.submissionNumContainer, false)

            val difficultyTextView: TextView = solvedView.findViewById(R.id.difficultyTextView)
            val countTextView: TextView = solvedView.findViewById(R.id.countTextView)
            val submissionsTextView: TextView = solvedView.findViewById(R.id.submissionsTextView)

            difficultyTextView.text = solved.difficulty
            submissionsTextView.text= "Submissions: ${solved.submissions}"
            countTextView.text = "Count: ${solved.count}"
            binding.submissionNumContainer.addView(solvedView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }
}
