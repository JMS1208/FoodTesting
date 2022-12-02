package com.capstone.foodtesting.ui.common.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentCommonCodeGenerateBinding
import com.capstone.foodtesting.util.CommonFunc
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommonCodeGenerateFragment : Fragment() {

    private var _binding: FragmentCommonCodeGenerateBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CommonCodeGenerateViewModel>()

    private val args by navArgs<CommonCodeGenerateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonCodeGenerateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {

            val result = viewModel.getStoreInfoByRegNum(args.regNum)

            if (result.isSuccessful) {
                result.body()?.get(0)?.market?.let { restaurant->
                    createQRCode(restaurant.reg_num)

                    restaurant.name?.let {
                        binding.tvRestaurantName.text = it
                    }

                    restaurant.category?.let {
                        binding.tvRestaurantName.append(" [$it]")
                    }
                }
            } else {
                findNavController().popBackStack()
                CommonFunc.showToast(requireContext(), "매장 먼저 등록해주세요")
            }

        }


        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun createQRCode(data: String) {
        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            Glide.with(requireContext())
                .load(bmp)
                .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_logo))
                .into(binding.ivQRCode)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}