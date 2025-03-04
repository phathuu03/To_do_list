package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.activity.ChooseLanguageActivity
import com.example.myapplication.databinding.FragmentSettingBinding
import com.example.myapplication.utils.Utils


class SettingFragment : Fragment() {

    private val binding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val language = Utils.getData(requireContext(), "language", "")
        val languageCurrent = when(language){
            "en" -> getString(R.string.txt_english)
            "vi" -> getString(R.string.txt_vn)
            "kr" -> getString(R.string.txt_korea)
            else -> ""
        }

        binding.tvLanguageCurrentApp.text = languageCurrent
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutLanguage.setOnClickListener {
            val intent = Intent(requireContext() , ChooseLanguageActivity::class.java)
            startActivity(intent)
        }
    }


}