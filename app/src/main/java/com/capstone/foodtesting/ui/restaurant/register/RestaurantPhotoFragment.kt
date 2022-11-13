package com.capstone.foodtesting.ui.restaurant.register

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterAddPhotoBinding
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.util.CommonFunc.showToast
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.delay
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
                    uri.path?.let {


                        viewLifecycleOwner.lifecycleScope.launch {
                            val dialog = DialogLoading()
                            dialog.show(childFragmentManager, dialog.tag)

                            //TODO {아래 주석 풀어야함}
//                            val result = viewModel.uploadRestaurantImage(it)
//
//
//                            if (result.isSuccessful) {
//                                showToast(requireContext(),"해당 이미지를 사용할 수 있습니다")
//                                Glide.with(requireContext())
//                                    .load(it)
//                                    .into(binding.ivSelectedImage)
//                            } else {
//                                showToast(requireContext(),"사진 등록에 실패하였습니다")
//                            }
                            delay(4000L)

                            dialog.dismiss()
                        }


                    } ?: showToast(requireContext(),"사진의 경로를 파악할 수 없습니다")


                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



}