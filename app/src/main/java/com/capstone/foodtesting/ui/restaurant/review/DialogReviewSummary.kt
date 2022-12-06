package com.capstone.foodtesting.ui.restaurant.review

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentReviewSummaryBinding
import com.capstone.foodtesting.util.dialogFragmentResize
import com.capstone.foodtesting.util.sandboxAnimations
import java.lang.NullPointerException


class DialogReviewSummary : DialogFragment()  {

    private var _binding: FragmentReviewSummaryBinding? = null
    private val binding get() = _binding!!

    private lateinit var message: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.TransparentDialogDim)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewSummaryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this::message.isInitialized) {
            binding.tvReviewSummary.text = message
        }

        setupLottieView()

        requireContext().dialogFragmentResize(this, 1f,1f)
    }

    private fun setupLottieView() {
        binding.lvRobot.apply {
            sandboxAnimations()
            playAnimation()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        _binding = null
        super.onDismiss(dialog)
    }

    fun setMessage(message: String) {
        this.message = message

        try {
            binding.tvReviewSummary.text = message
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}