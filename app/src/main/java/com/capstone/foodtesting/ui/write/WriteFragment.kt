package com.capstone.foodtesting.ui.write

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.date.DateTime
import com.capstone.foodtesting.databinding.FragmentWriteBinding
import com.capstone.foodtesting.util.Constants.DATE_TIME_FORMAT
import com.capstone.foodtesting.util.Constants.DATE_TIME_KEY
import com.capstone.foodtesting.util.Constants.SELECT_DATE_TIME
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import timber.log.Timber.tag
import java.text.SimpleDateFormat

@AndroidEntryPoint
class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WriteViewModel>()

    private var startDate: DateTime = DateTime(whenOption = DateTime.WHEN.START)

    private var endDate: DateTime = DateTime(whenOption = DateTime.WHEN.END)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivMenuImage.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start {

                    Glide.with(requireContext())
                        .load(it)
                        .into(binding.ivMenuImage)

                }
        }

        binding.etNewMenuName.apply {
            text = Editable.Factory.getInstance().newEditable(viewModel.menuName)
            addTextChangedListener { text: Editable? ->
                text?.let {
                    val menuName = it.toString()
                    if (menuName.isNotEmpty()) {
                        viewModel.menuName = menuName
                    }
                }

            }
        }

        binding.cvStartDate.setOnClickListener {
            val action = WriteFragmentDirections.actionFragmentWriteToWriteDateTimeDialogFragment(startDate)
            findNavController().navigate(action)
        }

        binding.cvEndDate.setOnClickListener {
            val action = WriteFragmentDirections.actionFragmentWriteToWriteDateTimeDialogFragment(endDate)
            findNavController().navigate(action)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            SELECT_DATE_TIME, viewLifecycleOwner
        ) { _, bundle ->


            val dateTime: DateTime? = bundle.getParcelable<DateTime>(DATE_TIME_KEY)

            dateTime?.let {
                if(dateTime.whenOption == DateTime.WHEN.START) {
                    binding.tvStartDate.text =
                        SimpleDateFormat(DATE_TIME_FORMAT).format(dateTime.getDateTime())
                    startDate = it
                } else {
                    binding.tvEndDate.text =
                        SimpleDateFormat(DATE_TIME_FORMAT).format(dateTime.getDateTime())
                    endDate = it
                }
            }



    }


}


override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

}