package com.capstone.foodtesting.ui.loading

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.DialogLoadingBinding
import com.capstone.foodtesting.util.sandboxAnimations
import com.skydoves.balloon.balloon
import java.lang.NullPointerException

class DialogLoading: DialogFragment() {

    private var _binding: DialogLoadingBinding? = null
    private val binding get() = _binding!!

    private lateinit var message: String


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return Dialog(requireContext(), R.style.TransparentDialogDim)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this::message.isInitialized) {
            binding.tvLoading.text = message
        }

        binding.lottieView.apply {
            sandboxAnimations()
            playAnimation()
        }

        binding.tvLoading.setOnClickListener {
            dismiss()
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        _binding = null
        super.onDismiss(dialog)
    }

    fun setMessage(message: String) {
        this.message = message

        try {
            binding.tvLoading.text = message
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}