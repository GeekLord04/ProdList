package com.geekster.prodlist.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.geekster.prodlist.R
import com.geekster.prodlist.Viewmodel.GetProductViewModel
import com.geekster.prodlist.databinding.FragmentGetProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetProductFragment : Fragment() {

    private var _binding : FragmentGetProductBinding? = null
    private val binding get() = _binding!!

    private val Listproductviewmodel by viewModels<GetProductViewModel>()

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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}