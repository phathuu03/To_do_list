package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStartBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StartFragment : Fragment() {
    private val binding by lazy {
        FragmentStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = findNavController()

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            val action = StartFragmentDirections.actionStartFragmentToChooseLanguageFragment()
            controller.navigate(action, NavOptions.Builder()
                .setPopUpTo(R.id.startFragment, true)
                .build()
            )

        }


    }


}