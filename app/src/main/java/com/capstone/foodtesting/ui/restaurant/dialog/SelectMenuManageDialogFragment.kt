package com.capstone.foodtesting.ui.restaurant.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.capstone.foodtesting.databinding.BottomSheetSelectMenuManageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectMenuManageDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSelectMenuManageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return BottomSheetDialog(requireContext(), theme)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetSelectMenuManageBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAddMenu.setOnClickListener {
            parentFragment?.apply {
                setFragmentResult("addMenu",Bundle().apply {
                    this.putBoolean("addMenu",true)
                })
                dismiss()
            }
        }

        binding.tvModifyMenu.setOnClickListener {

        }
    }
}