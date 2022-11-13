package com.capstone.foodtesting.ui.restaurant.menu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.date.DateTime
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.FragmentRestaurantAddMenuBinding
import com.capstone.foodtesting.ui.restaurant.dialog.TestingPeriodDialogFragment

import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RestaurantAddMenuFragment : Fragment() {

    private var _binding: FragmentRestaurantAddMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RestaurantAddMenuViewModel>()

    private var startDate: Date = Date(System.currentTimeMillis())

    private var endDate: Date = Date(System.currentTimeMillis())

    private lateinit var marketInfo: Restaurant

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantAddMenuBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMemberInfo.collectLatest { member->
                    member?.let {
                        val result = viewModel.getMarketInfo(it.uuid)

                        if (result.isSuccessful) {
                            result.body()?.get(0)?.market?.let {
                                marketInfo = it
                            }
                        }
                    }
                }

            }
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
//
        binding.ivAddMenuImage.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start {
                    Glide.with(requireContext())
                        .load(it)
                        .into(binding.ivMenuImage)

                }
        }

        binding.tvTestingPeriod.setOnClickListener {
            val dialog = TestingPeriodDialogFragment()
            dialog.show(childFragmentManager, dialog.tag)

        }
//
//        binding.etNewMenuName.apply {
//            text = Editable.Factory.getInstance().newEditable(viewModel.menuName)
//            addTextChangedListener { text: Editable? ->
//                text?.let {
//                    val menuName = it.toString()
//                    if (menuName.isNotEmpty()) {
//                        viewModel.menuName = menuName
//                    }
//                }
//
//            }
//        }
//
//        binding.cvStartDate.setOnClickListener {
//            val action =
//                WriteFragmentDirections.actionFragmentWriteToWriteDateTimeDialogFragment(startDate)
//            findNavController().navigate(action)
//        }
//
//        binding.cvEndDate.setOnClickListener {
//            val action =
//                WriteFragmentDirections.actionFragmentWriteToWriteDateTimeDialogFragment(endDate)
//            findNavController().navigate(action)
//        }
//
//        requireActivity().supportFragmentManager.setFragmentResultListener(
//            SELECT_DATE_TIME, viewLifecycleOwner
//        ) { _, bundle ->
//
//
//            val dateTime: DateTime? = bundle.getParcelable<DateTime>(DATE_TIME_KEY)
//
//            dateTime?.let {
//                if (dateTime.whenOption == DateTime.WHEN.START) {
//                    binding.tvStartDate.text =
//                        SimpleDateFormat(DATE_TIME_FORMAT).format(dateTime.getDateTime())
//                    startDate = it
//                } else {
//                    binding.tvEndDate.text =
//                        SimpleDateFormat(DATE_TIME_FORMAT).format(dateTime.getDateTime())
//                    endDate = it
//                }
//            }
//
//
//        }

        setFragmentResultListener("TestingPeriod") { _, bundle ->

            startDate = Date(bundle.getLong("StartDate"))
            endDate = Date(bundle.getLong("EndDate"))
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E)")
            val text = "${simpleDateFormat.format(startDate)} ~ ${simpleDateFormat.format(endDate)}"

            binding.tvTestingPeriod.text = text
            binding.tvTestingPeriod.setTextColor(ContextCompat.getColor(requireContext(), R.color.point_red))

        }

        binding.btnAdd.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val menuName = binding.etMenuName.text.toString()
                val hopePrice = binding.etHopePrice.text.toString()
                val testPrice = binding.etSalePrice.text.toString()
                val menuExplains = binding.etMenuExplains.text.toString()
                val menuIngredients = binding.etIngredients.text.toString()
                val startDate = this@RestaurantAddMenuFragment.startDate.time
                val endDate = this@RestaurantAddMenuFragment.endDate.time
                val market_reg_num = marketInfo.reg_num
                val market_uuid = marketInfo.customer_id
                val postDate = System.currentTimeMillis()
                val updateDate = System.currentTimeMillis()



                val menu = Menu(UUID.randomUUID().toString(),
                    menuName,
                    market_reg_num.toString(),
                    market_uuid.toString(),
                    hopePrice.toInt(),
                    testPrice.toInt(),
                    startDate,
                    endDate,
                    menuIngredients,
                    postDate,
                    updateDate,
                    menuExplains
                    )

                val result = viewModel.postNewMenu(menu)

                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), "정상적으로 등록되었습니다", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}