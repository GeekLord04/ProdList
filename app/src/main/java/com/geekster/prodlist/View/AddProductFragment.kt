package com.geekster.prodlist.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.geekster.prodlist.R
import com.geekster.prodlist.Viewmodel.AddProductViewModel
import com.geekster.prodlist.databinding.FragmentAddProductBinding
import com.geekster.prodlist.utils.Constants.TAG
import com.geekster.prodlist.utils.FileUtils
import com.geekster.prodlist.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private val addViewModel by viewModels<AddProductViewModel>()

    private var selectedImageFile: File? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_SHORT)
            }
        }

    private val chooseImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val typeOfImage = context?.contentResolver?.getType(uri)
                    if (typeOfImage == "image/jpeg" || typeOfImage == "image/png" || typeOfImage == "image/jpg"){
                        val filePath = FileUtils.getPath(requireContext(), uri)
                        selectedImageFile = File(filePath)
                        binding.imageView.setImageURI(uri)
                    }

                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectImage.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        binding.btnAddProduct.setOnClickListener {
            if (inputValidation()) {
                val productName = binding.etProductName.text.toString()
                val productType = binding.etProductType.text.toString()
                val price = binding.etPrice.text.toString()
                val tax = binding.etTax.text.toString()

                addViewModel.addProduct(productName, productType, price, tax, selectedImageFile)
                lifecycleScope.launch {
                    bindObserver()
                }
            }

        }


    }

    private fun bindObserver() {
        addViewModel.addProductLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("${it.message}")
                    Log.d(TAG, "Error: ${it.data} and ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Success -> {
                    Log.d(TAG, "Add Item Data - ${it.data}")
                    binding.progressBar.visibility = View.GONE
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun inputValidation() : Boolean {
        val productName = binding.etProductName.text.toString()
        val productType = binding.etProductType.text.toString()
        val price = binding.etPrice.text.toString()
        val tax = binding.etTax.text.toString()

        if (productName.isEmpty()) {
            showToast("Please enter Product name")
            return false
        }

        if (productType.isEmpty()) {
            showToast("Please enter Product type")
            return false
        }

        if (price.isEmpty()) {
            showToast("Please enter the Price")
            return false
        }

        if (tax.isEmpty()) {
            showToast("Please enter the Tax")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkPermissionAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        chooseImageLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}