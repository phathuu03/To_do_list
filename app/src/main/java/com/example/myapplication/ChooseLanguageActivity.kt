package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.IntroActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.adapter.LanguageAdapter
import com.example.myapplication.databinding.FragmentChooseLanguageBinding
import com.example.myapplication.model.LanguageApplication
import com.example.myapplication.utils.LocaleHelper
import com.example.myapplication.utils.Utils

class ChooseLanguageActivity : AppCompatActivity(), LanguageAdapter.OnItemSelectedListener {
    private lateinit var adapter: LanguageAdapter
    private lateinit var binding: FragmentChooseLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChooseLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentLanguage = Utils.getData(this, "language", "")
        adapter = LanguageAdapter(this, currentLanguage, this)
        binding.recycleViewLanguage.layoutManager = LinearLayoutManager(this)
        binding.recycleViewLanguage.adapter = adapter

        val isStarted = Utils.getData(this, "isStarted", false)

        binding.btnSelectedLanguage.setOnClickListener {
            val language = Utils.getData(this, "language", "")
            LocaleHelper.setLocale(this, language)

            if (isStarted) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Nếu bạn có màn hình intro, điều hướng đến đó
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onItemSelected(item: LanguageApplication, position: Int) {
        adapter.updateSelection(position)
        Utils.saveData(this, "language", item.typeLanguage.toString().lowercase())
    }
}