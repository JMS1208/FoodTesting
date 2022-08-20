package com.capstone.foodtesting.ui.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentCodeGenerateBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*


class CodeGenerateFragment : Fragment() {

    private var _binding: FragmentCodeGenerateBinding? = null
    private val binding get() = _binding!!

    private lateinit var ivQRCode:ImageView
    private lateinit var btnGenerateQRCode:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeGenerateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivQRCode=binding.ivQRCode
        btnGenerateQRCode=binding.btnGenerateQRCode

        btnGenerateQRCode.setOnClickListener{
            // 임의의 UUID 생성
            val data=UUID.randomUUID()

            val writer=QRCodeWriter()
            try{
                val bitMatrix=writer.encode(data.toString(), BarcodeFormat.QR_CODE,512,512)
                val width=bitMatrix.width
                val height=bitMatrix.height
                val bmp=Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
                for(x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y,if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                ivQRCode.setImageBitmap(bmp)
            }catch (e:WriterException){
                e.printStackTrace()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}