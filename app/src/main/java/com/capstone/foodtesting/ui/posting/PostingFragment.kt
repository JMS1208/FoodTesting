package com.capstone.foodtesting.ui.posting

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentPostingBinding
import com.capstone.foodtesting.ui.posting.adapter.HashTagAdapter
import com.capstone.foodtesting.ui.posting.adapter.MenuAdapter
import com.capstone.foodtesting.ui.posting.adapter.ReviewAdapter
import com.capstone.foodtesting.util.*
import com.google.android.material.tabs.TabLayout
import com.skydoves.balloon.*
import com.taufiqrahman.reviewratings.Bar
import com.taufiqrahman.reviewratings.BarLabels
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.abs


@AndroidEntryPoint
class PostingFragment : Fragment() {

    private var _binding: FragmentPostingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PostingViewModel>()


    private lateinit var menuAdapter: MenuAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupMenuRecyclerView()

        binding.ivTypeExplains.setOnClickListener {
            val iconForm = IconForm.Builder(requireContext())
                .setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_info))
                .setIconColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setIconSize(50)
                .build()

            val balloon = Balloon.Builder(requireContext())
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText("사장님이 테스트 받고 싶은 유형입니다.\n해당 유형에 대한 피드백을 남길 수 있습니다.")
                .setTextColorResource(R.color.white)
                .setTextSize(20f)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setIconForm(iconForm)
                .setCornerRadius(8f)
                .setBackgroundColorResource(R.color.personal_color2)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(viewLifecycleOwner)
                .setTextTypeface(Typeface.SANS_SERIF)
                .build()
            balloon.showAlignTop(binding.ivTypeExplains)
        }

    }



    private fun setupMenuRecyclerView() {
        val menuList = listOf("메뉴1", "메뉴2", "메뉴3", "메뉴4", "메뉴5", "메뉴6", "메뉴7", "메뉴8", "메뉴9", "메뉴10")
        menuAdapter = MenuAdapter(menuList)

        binding.rvMenu.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        }
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}