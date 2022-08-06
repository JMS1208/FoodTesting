package com.capstone.foodtesting.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentHomeBinding
import com.capstone.foodtesting.databinding.ItemBannerBinding
import com.capstone.foodtesting.ui.home.bottomsheet.BSBannerFragment
import com.capstone.foodtesting.util.Constants.HOME_BANNER_DURATION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var job: Job

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var imageList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        return binding.root

    }

    override fun onResume() {
        super.onResume()
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun scrollJobCreate() {
        job = lifecycleScope.launchWhenResumed {

            delay(HOME_BANNER_DURATION)
            val position = binding.viewPager.currentItem
            val newPosition = (position + 1) % imageList.size
            binding.viewPager.setCurrentItem(newPosition, true)

        }

    }


    private inner class ViewPagerAdapter(
        private val items: MutableList<String>,
        private val itemSize: Int
    ) :
        RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
        inner class ViewHolder(val itemBinding: ItemBannerBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(url: String?) {

                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                circularProgressDrawable.apply {
                    strokeWidth = 10f
                    centerRadius = 40f
                    setTint(resources.getColor(R.color.bright_grey))
                    start()
                }

                url?.let {
                    Glide.with(requireContext())
                        .load(it)
                        .placeholder(circularProgressDrawable)
                        .transform(CenterCrop())
                        .into(itemBinding.ivBannerHome)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemBinding =
                ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val newPosition = position % itemSize
            val item = items[newPosition]
            holder.bind(item)
            // TODO {나중에 시간되면, 요기요처럼 바꾸기}
        }

        override fun getItemCount(): Int = Int.MAX_VALUE
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageList = mutableListOf(
            "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1609501677070-800d6d157367?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80"
        ) // 원래 백에서 이미지 리스트 받아와야함

        val itemSize = imageList.size // 맨 처음 받아왔을때 원래 사이즈


        viewPagerAdapter = ViewPagerAdapter(imageList, itemSize)

        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val progressText =
                    "${position % itemSize + 1} / $itemSize 모두보기" // 현재 배너 페이지 표시
                binding.btnBanner.text = progressText

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                when (state) {

                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        job.cancel()
                    }
                    else -> {
                        if (!job.isActive) {
                            scrollJobCreate()
                        }
                    }
                }
            }
        })

        binding.btnBanner.setOnClickListener {

            val bottomSheet = BSBannerFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.root
    }


}