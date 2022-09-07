package com.capstone.foodtesting.ui.qr

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentCodeScanBinding
import com.capstone.foodtesting.util.Constants.CODE_SCAN_BUNDLE_KEY
import com.capstone.foodtesting.util.Constants.CODE_SCAN_REQUEST_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CodeScanFragment : Fragment() {

    private var _binding: FragmentCodeScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeScanBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    private val contract = ActivityResultContracts.RequestPermission()

    private val activityResultLauncher by lazy {

        registerForActivityResult(contract) { isGranted ->
            if (isGranted) {
                startScanning()
                if(::codeScanner.isInitialized) {
                    codeScanner.startPreview()
                }
            } else {
                createDialog()
            }

        }
    }

    private fun createDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("권한 설정")
            setMessage("QR 코드 스캔을 위해 카메라 권한을 허용해주세요")
            setPositiveButton("권한 설정하러 가기"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:${requireActivity().packageName}"))
                    startActivity(intent)
                } catch (E: ActivityNotFoundException) {
                    E.printStackTrace()
                    val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                    startActivity(intent)
                }

            }
            setNegativeButton("취소하기"
            ) { _, _ -> findNavController().popBackStack() }
            create()
            show()
        }
    }

    private fun requestCameraPermission() {
        activityResultLauncher.launch(Manifest.permission.CAMERA)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestCameraPermission()

        binding.scannerView.setOnClickListener {
            if(::codeScanner.isInitialized){
                codeScanner.startPreview()
            } else {
                startScanning()
                codeScanner.startPreview()
            }

        }
    }

    private fun startScanning() {

        codeScanner = CodeScanner(requireContext(), binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isTouchFocusEnabled = true
            isFlashEnabled = false
        }



        codeScanner.decodeCallback = DecodeCallback {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), "Scan Result: ${it.text}", Toast.LENGTH_SHORT)
                    .show()

                requireActivity().supportFragmentManager.setFragmentResult(CODE_SCAN_REQUEST_KEY,
                    Bundle().apply {
                        putString(CODE_SCAN_BUNDLE_KEY, "아무 매장명이나 전달")
                    }
                    )

                findNavController().popBackStack()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), "Camera 초기화 오류 ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }



    override fun onResume() {
        super.onResume()
        if(::codeScanner.isInitialized){
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if(::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}