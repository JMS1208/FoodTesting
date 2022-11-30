package com.capstone.foodtesting.ui.common.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.databinding.DialogAddressSettingBinding
import com.capstone.foodtesting.ui.bottomsheet.setaddress.adapter.AddressSearchPagingAdapter
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.Constants
import com.capstone.foodtesting.util.dialogFragmentResize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddressSettingsDialogFragment: DialogFragment() {

    private var _binding: DialogAddressSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddressSettingsViewModel>()

    private lateinit var addressSearchPagingAdapter: AddressSearchPagingAdapter

    private var longitude: Double? = null //경도가 x
    private var latitude: Double? = null //위도가 y

    private var document: Document? = null
    private var addressInfo: AddressInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddressSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressSearchPagingAdapter = AddressSearchPagingAdapter().apply {
            setOnItemClickListener { document ->
                //검색결과 클릭시
                this@AddressSettingsDialogFragment.document = document
                CommonFunc.showToast(requireContext(), "\'${document.addressName}\' 선택하셨습니다")
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val x = document.x
                    val y = document.y

                    if(x != null && y != null) {
                        viewModel.convertCoordToAddress(x, y).collectLatest { addressInfo->

                            this@AddressSettingsDialogFragment.addressInfo = addressInfo
                            this@AddressSettingsDialogFragment.longitude = x.toDouble()
                            this@AddressSettingsDialogFragment.latitude = y.toDouble()

                            addressInfo?.let {
                                withContext(Dispatchers.Main) {
                                    binding.tvSelectedAddress.text = addressInfo.address?.addressFullName ?: document.addressName
                                    binding.tvSelectedRoadAddress.text = "[ ${addressInfo.roadAddress?.roadAddressFullName ?: document.roadAddress?.addressName ?: "도로명 주소 없음"} ]"
                                }

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

        searchAddress()

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSelect.setOnClickListener {

            val addressText = addressInfo?.address?.addressFullName ?: document?.addressName

            val roadAddressText = addressInfo?.roadAddress?.roadAddressFullName ?: document?.roadAddress?.addressName ?: ""

            if (longitude != null && latitude != null && !addressText.isNullOrEmpty()) {
                parentFragment?.setFragmentResult("AddressSetting", Bundle().apply {
                    putDouble("Longitude", longitude!!)
                    putDouble("Latitude", latitude!!)
                    putString("Address", addressText)
                    putString("RoadAddress", roadAddressText )
                })
                dismiss()
            } else {
                CommonFunc.showToast(requireContext(),"주소를 선택해주세요")

            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.TransparentDialogDim)
    }

    private fun searchAddress() {
        val startTime: Long = System.currentTimeMillis()
        var endTime: Long

        binding.etSearchAddress.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= Constants.SEARCH_ADDRESS_DELAY_TIME) {
                text?.let {
                    val query = it.toString()
                    if (query.isNotEmpty()) {
                        viewModel.searchAddress(query)
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 0.9f, 0.8f)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}