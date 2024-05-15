package com.geekster.prodlist.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.geekster.prodlist.R
import com.geekster.prodlist.Viewmodel.GetProductViewModel
import com.geekster.prodlist.databinding.FragmentGetProductBinding
import com.geekster.prodlist.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GetProductFragment : Fragment() {

    private var _binding : FragmentGetProductBinding? = null
    private val binding get() = _binding!!

    private val getProductViewModel by viewModels<GetProductViewModel>()

    private lateinit var adapter : ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGetProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObserver()
        lifecycleScope.launch {
            getProductViewModel.getListData()
        }

        adapter = ItemAdapter(mutableListOf())
        binding.itemList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.itemList.adapter = adapter

    }

    private fun bindObserver() {
        getProductViewModel.listLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Error -> Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                is NetworkResult.Loading -> println("Loading")
                is NetworkResult.Success -> {

                    adapter.submitList(it.data)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}