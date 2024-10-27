package com.example.navbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navbar.databinding.FragmentSubmissionsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Submissions : Fragment() {

    private var _binding: FragmentSubmissionsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Intercept the back press in this fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val navOptions = NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.homeFragment, false) // Ensure homeFragment exists in nav graph
                        .build()

                    findNavController().navigate(R.id.homeFragment, null, navOptions)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubmissionsBinding.inflate(inflater, container, false)

        // Observe the username from SharedViewModel
        sharedViewModel.username.observe(viewLifecycleOwner) { username ->
            username?.let {
                fetchUserSubmissions(it)
            } ?: displayError("Username is null.")
        }

        return binding.root
    }

    private fun fetchUserSubmissions(username: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Call the API to get user submissions
                val userSubmission = RetrofitInstance.api.getUserSubmissions(username)
                withContext(Dispatchers.Main) {
                    displayUserSubmissions(userSubmission)
                }
            } catch (e: Exception) {
                displayError("Error fetching user submissions: ${e.message}")
            }
        }
    }

    private fun displayUserSubmissions(userSubmission: userSubmission) {
        binding.submissionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.submissionRecyclerView.adapter = SubmissionAdapter(userSubmission.submission)
    }

    private fun displayError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }
}
