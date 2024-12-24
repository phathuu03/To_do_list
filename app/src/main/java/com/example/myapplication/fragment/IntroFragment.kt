package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.ViewPage2IntroAdapter
import com.example.myapplication.databinding.FragmentIntroBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.relex.circleindicator.CircleIndicator3

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class IntroFragment : Fragment() {
    private lateinit var viewPage: ViewPager2
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

        viewPage = binding.viewPage2Intro
        viewPage.adapter = ViewPage2IntroAdapter(requireActivity())
        indicator = binding.indicatorIntro
        indicator.setViewPager(viewPage)
        navController = findNavController()
        val btnNext = binding.btnNextIntro

        btnNext.setOnClickListener {

            when (viewPage.currentItem) {
                0 -> {
                    viewPage.currentItem = 1
                }

                1 -> {
                    viewPage.currentItem = 2
                }

                2 -> {
                    val action = IntroFragmentDirections.actionIntroFragmentToHomeFragment()
                    navController.navigate(
                        action, NavOptions.Builder()
                            .setPopUpTo(R.id.introFragment, true)
                            .setPopUpTo(R.id.chooseLanguageFragment, true)
                            .build()
                    )
                    (requireActivity() as MainActivity).binding.menuNav.visibility = View.VISIBLE
                    (requireActivity() as MainActivity).binding.btnAdd.visibility = View.VISIBLE
                }

            }

        }


    }


}