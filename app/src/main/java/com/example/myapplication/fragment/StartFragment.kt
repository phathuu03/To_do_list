package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.activity.ChooseLanguageActivity
import com.example.myapplication.databinding.FragmentStartBinding
import com.example.myapplication.utils.LocaleHelper
import com.example.myapplication.utils.Utils
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

        val language = Utils.getData(requireContext() , "language" , "")
        LocaleHelper.setLocale(requireContext(), language)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            val intent = Intent(requireContext() , ChooseLanguageActivity::class.java)
            startActivity(intent)
            requireActivity().finish()


        }



    }


}