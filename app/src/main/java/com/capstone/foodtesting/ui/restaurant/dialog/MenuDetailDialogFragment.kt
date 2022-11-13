package com.capstone.foodtesting.ui.restaurant.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.databinding.DialogFragmentMenuDetailBinding
import com.capstone.foodtesting.util.dialogFragmentResize
import java.text.SimpleDateFormat
import java.util.*

class MenuDetailDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentMenuDetailBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<MenuDetailDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentMenuDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.menu.let { menu->
            binding.apply {
                tvMenuName.text = menu.menuName
                tvMenuExplains.text = menu.explains
                tvMenuIngredients.text = menu.ingredients
                Glide.with(requireContext())
                    .load(menu.photoUrl)
                    .into(ivMenuImage)
                val startDate = Date(menu.start_date)
                val endDate = Date(menu.end_date)
                val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E)")
                val dateText = "${simpleDateFormat.format(startDate)} ~ ${simpleDateFormat.format(endDate)}"
                tvTestingPeriod.text = dateText

            }
        }




    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.TransparentDialogDim)
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