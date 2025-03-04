package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.MainActivity
import com.example.myapplication.adapter.ViewPage2IntroAdapter
import com.example.myapplication.databinding.FragmentIntroBinding
import com.example.myapplication.utils.Utils
import me.relex.circleindicator.CircleIndicator3

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class IntroFragment : Fragment() {
    private lateinit var viewPage2: ViewPager2
    private lateinit var indicator: CircleIndicator3
    private lateinit var navController: NavController

    private val binding by lazy {
        FragmentIntroBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPage2 = binding.viewPage2Intro
        viewPage2.adapter = ViewPage2IntroAdapter(requireActivity())
        indicator = binding.indicatorIntro
        indicator.setViewPager(viewPage2)
        navController = findNavController()
        val btnNext = binding.btnNextIntro

        btnNext.setOnClickListener {

            when (viewPage2.currentItem) {
                0 -> {
                    viewPage2.currentItem = 1
                }

                1 -> {
                    viewPage2.currentItem = 2
                }

                2 -> {
                    Utils.saveData(requireContext(), "isStarted", true)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

            }

        }


    }


}