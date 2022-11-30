package com.capstone.foodtesting.ui.bottomsheet.setaddress

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.databinding.BottomSheetSettingAddressBinding
import com.capstone.foodtesting.ui.bottomsheet.setaddress.adapter.AddressInfoAdapter
import com.capstone.foodtesting.ui.bottomsheet.setaddress.adapter.AddressSearchPagingAdapter
import com.capstone.foodtesting.util.CommonFunc.showTooltip
import com.capstone.foodtesting.util.Constants.SEARCH_ADDRESS_DELAY_TIME
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import it.sephiroth.android.library.xtooltip.Tooltip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class BSSetupAddrFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSettingAddressBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BSSetupAddrViewModel>()

    //권한 요청
    private val contract = ActivityResultContracts.RequestMultiplePermissions()

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var addressInfoAdapter: AddressInfoAdapter

    private lateinit var addressSearchPagingAdapter: AddressSearchPagingAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(contract) { permissions ->
            val notPermittedList = permissions.filter { permission -> !permission.value }
            if (notPermittedList.isNotEmpty()) {
                createDialog()
            } else {
                //registerLocationUpdate()
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener { dialogInterface ->

            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.peekHeight = 0
                behaviour.isHideable = true
                behaviour.isDraggable = true
                behaviour.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) { //뒷배경 안 사라져서 collapsed 일 때 dismiss 해줌
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }

                })
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSettingAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vBar.setOnClickListener {
            dismiss()
        }

        binding.tvSetupCurrentLocation.setOnClickListener {
            requestLocationPermission()
            registerLocationUpdate()
        }


        addressInfoAdapter = AddressInfoAdapter()
        addressInfoAdapter.setOnItemRemoveListener { addressInfo ->
            viewModel.deleteAddressInfo(addressInfo)
        }
        addressInfoAdapter.setOnItemClickListener { addressInfo ->

            addressInfo.date = Date(System.currentTimeMillis())
            viewModel.insertAddressInfo(addressInfo)
            dismiss()
        }



        binding.rvLocationRecords.apply {
            adapter = addressInfoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.savedAllAddressInfo.collectLatest {
                it.mapIndexed { index, addressInfo ->
                    addressInfo.isFirstItem = index == 0
                }
                addressInfoAdapter.submitList(it)
                if(it.isNotEmpty()) {
                    binding.rvLocationRecords.smoothScrollToPosition(0)
                }

            }
        }


        binding.tvRemoveAllRecords.setOnClickListener {
            viewModel.deleteAllAddressInfo()
        }



        searchAddress()

        addressSearchPagingAdapter = AddressSearchPagingAdapter().apply {
            setOnItemClickListener {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val x = it.x
                    val y = it.y

                    if(x != null && y != null) {
                        viewModel.convertCoordToAddress(x, y).collectLatest { addressInfo->
                            addressInfo?.let {
                                viewModel.insertAddressInfo(it.apply {
                                    this.x = x
                                    this.y = y
                                })
                            }

                        }
                    }

                }

            }
        }

        binding.rvSearchResult.apply {
            adapter = addressSearchPagingAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagingResult.collectLatest {
                    addressSearchPagingAdapter.submitData(it)

                }
            }
        }



        binding.rvLocationRecords.setOnTouchListener { v, motionEvent ->

            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(motionEvent)

            true
        }


    }



    private fun searchAddress() {
        val startTime: Long = System.currentTimeMillis()
        var endTime: Long

        binding.etSearchAddress.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_ADDRESS_DELAY_TIME) {
                text?.let {
                    val query = it.toString()
                    if (query.isNotEmpty()) {
                        viewModel.searchAddress(query)
                    }
                }
            }

        }
    }

    private fun registerLocationUpdate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                //currentLocation = locationResult.lastLocation
                //여기서 myLocation 교체되는 콜백 지정
                locationResult.lastLocation?.let {
                    viewLifecycleOwner.lifecycleScope.launch {

                        viewModel.convertCoordToAddress(it).collect { addressInfo ->
                            addressInfo?.let {
                                viewModel.insertAddressInfo(it)
                            }
                        }

                    }
                }


                if (::locationCallback.isInitialized) {
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }

            }
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener {
            requestLocationPermission()
        }

        task.addOnSuccessListener {
            //여기서 위치요청
            //퍼미션 허용되어 있다면 if문 실행
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private fun createDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("권한 설정")
            setMessage("현재 위치를 가져오기 위한 위치 권한을 허용해주세요")
            setPositiveButton(
                "권한 설정하러 가기"
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
            setNegativeButton(
                "취소하기"
            ) { _, _ -> findNavController().popBackStack() }
            create()
            show()
        }
    }



}