package com.capstone.foodtesting.ui.restaurant.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterAddPhotoBinding
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.util.CommonFunc
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class RestaurantPhotoFragment : Fragment() {

    private var _binding : FragmentRestaurantRegisterAddPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        (parentFragment as RestaurantRegisterFragment).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantRegisterAddPhotoBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAddImage.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start { uri ->
                    uri.let {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val dialog = DialogLoading()
                            dialog.show(childFragmentManager, dialog.tag)

                            val result =
                                viewModel.uploadRestaurantImage(it)


                            val jobResult = viewLifecycleOwner.lifecycleScope.async {
                                if (result.isSuccessful) {
                                    result.body()?.imageHash?.let { imageHash->

                                        Glide.with(requireContext())
                                            .load(it)
                                            .into(binding.ivSelectedImage)

                                        (parentFragment as RestaurantRegisterFragment).getCurrentRestaurant().apply {
                                            this.photoUrl = imageHash
                                        }

                                        "?????? ???????????? ????????? ??? ????????????"

                                    } ?: "?????? ????????? ?????????????????????"

                                } else {

                                    "?????? ????????? ?????????????????????"
                                }
                            }

                            if (dialog.isVisible) {
                                dialog.dismiss()
                            }

                            CommonFunc.showToast(requireContext(), jobResult.await() )


                        }
                    }

                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



}