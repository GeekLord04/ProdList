package com.geekster.prodlist.View

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

        if (isInternetAvailable()) {
            lifecycleScope.launch {
                getProductViewModel.getListData()
            }
        } else {
            Toast.makeText(requireContext(), "No internet connection available", Toast.LENGTH_SHORT).show()
        }

        adapter = ItemAdapter(mutableListOf())
        binding.itemList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.itemList.adapter = adapter


        binding.addItemBtn.setOnClickListener {
            findNavController().navigate(R.id.action_getProductFragment_to_addProductFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                getProductViewModel.getListData()

            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return false
            }

        })

        binding.itemList.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.searchView.clearFocus()

            }
            return@setOnTouchListener false
        }

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun bindObserver() {
        getProductViewModel.listLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is NetworkResult.Success -> {
                    adapter.setOriginalList(it.data ?: emptyList())
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}