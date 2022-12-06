package com.capstone.foodtesting.ui.common.manage.favorite

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.DialogFragmentFavoriteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

@AndroidEntryPoint
class FavoriteDialogFragment() : BottomSheetDialogFragment() {

    private var _binding: DialogFragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var favoriteRestaurantList: List<Restaurant>? = null

    private val viewModel by viewModels<FavoriteDialogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(
            requireContext(), R.style.TransparentDialog
        )

        dialog.apply {
            window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)

            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            dialog.setOnShowListener { dialogInterface ->

                val bottomSheetDialog = dialogInterface as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { bottomSheet ->
                    val layoutParams = bottomSheet.layoutParams
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    bottomSheet.layoutParams = layoutParams
                }
            }
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()
//        requireContext().dialogFragmentResize(this, 1f, 1f)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllFavoriteRestaurant().collectLatest { favoriteList ->
                Log.d("TAG", "즐겨찾기: $favoriteList")
                favoriteList?.let {
                    favoriteRestaurantList = it
                    setupFavoriteRestaurantViewPager(it)


                    withContext(Dispatchers.Main) {
                        if (it.isEmpty()) {
                            binding.tvRestaurantName.text = ""
                        }

                        binding.lvEmpty.isVisible = it.isEmpty()
                    }
                }

            }
        }

        binding.tvDelete.setOnClickListener {
            favoriteRestaurantList?.let { favoriteRestaurantList ->
                if (favoriteRestaurantList.isNotEmpty()) {
                    val idx = binding.vpFavorite.currentItem
                    val restaurant = favoriteRestaurantList[idx]
                    viewModel.deleteFavoriteRestaurant(restaurant)
                }
            }

        }

        binding.tvVisit.setOnClickListener {

        }


    }

    private fun setupFavoriteRestaurantViewPager(restaurantList: List<Restaurant>) {
        binding.vpFavorite.apply {

            adapter = FavoriteAdapter(restaurantList)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.95f + r * 0.05f

                }
            }

            setPageTransformer(compositePageTransformer)
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    favoriteRestaurantList?.let {
                        val restaurant = it[position]
                        val nameCategory = "\" ${restaurant.name} (${restaurant.category}) \""
                        binding.tvRestaurantName.text = nameCategory
                    }
                }
            })
        }

    }


}