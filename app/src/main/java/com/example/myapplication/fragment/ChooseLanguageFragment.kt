package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.LanguageAdapter
import com.example.myapplication.databinding.FragmentChooseLanguageBinding
import com.example.myapplication.model.LanguageApplication


class ChooseLanguageFragment : Fragment(), LanguageAdapter.OnItemSelectedListener {
    private lateinit var adapter: LanguageAdapter

private val binding by lazy {
    FragmentChooseLanguageBinding.inflate(layoutInflater)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recyclerView = binding.recycleViewLanguage
        adapter = LanguageAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = findNavController()
        binding.btnSelectedLanguage.setOnClickListener {
            controller.navigate(R.id.action_chooseLanguageFragment_to_introFragment)

        }


    }

    override fun onItemSelected(item: LanguageApplication, position: Int) {
        adapter.updateSelection( position)
    }


}