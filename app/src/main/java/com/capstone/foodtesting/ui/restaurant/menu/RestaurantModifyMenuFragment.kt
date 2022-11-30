package com.capstone.foodtesting.ui.restaurant.menu

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentRestaurantModifyMenuBinding
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.CommonFunc.imageRemoveIPPrefix
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.CommonFunc.showTooltip
import com.capstone.foodtesting.util.sandboxAnimations
import com.skydoves.balloon.ArrowOrientation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantModifyMenuFragment : Fragment() {

    private var _binding: FragmentRestaurantModifyMenuBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RestaurantModifyMenuFragmentArgs>()

    private val viewModel by viewModels<RestaurantModifyMenuViewModel>()

    private lateinit var menuAdapter: ModifyMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantModifyMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Log.d("TAG", "프린트 Args: ${args.regNum}")

        initToolbarView()

        menuAdapter = ModifyMenuAdapter()
        menuAdapter.setOnItemClickListener { menu ->
            val action =
                RestaurantModifyMenuFragmentDirections.actionFragmentRestaurantModifyMenuToFragmentRestaurantModifyMenuDetail(
                    menu
                )
            findNavController().navigate(action)

        }
        menuAdapter.setOnItemLongClickListener { menu, itemView ->
            val popupMenu = PopupMenu(requireContext(), itemView)

            popupMenu.apply {
                inflate(R.menu.menu_popup)

                setOnMenuItemClickListener { menuItem ->

                    when (menuItem.itemId) {
                        R.id.menu_item_modify -> {//메뉴 수정하기
                            val action =
                                RestaurantModifyMenuFragmentDirections.actionFragmentRestaurantModifyMenuToFragmentRestaurantModifyMenuDetail(
                                    menu
                                )
                            findNavController().navigate(action)
                            true
                        }

                        R.id.menu_item_break -> { //메뉴 품절관리하기
                            viewLifecycleOwner.lifecycleScope.launch {
                                menu.apply {
                                    is_break = if (is_break == 0) {
                                        1
                                    } else {
                                        0
                                    }
                                    photoUrl = imageRemoveIPPrefix(photoUrl)
                                }

                                val response = viewModel.modifyMenu(menu)

                                if (response.isSuccessful) {
                                    val message = response.body()?.message

                                    if (message == "Success to modify menu") {
                                        if (menu.is_break == 0) {
                                            showToast(requireContext(), "품절 처리를 철회하였습니다")
                                        } else {
                                            showToast(requireContext(), "품절 처리 되었습니다")
                                        }
                                        viewModel.fetchRestaurantInfoByRegNum(args.regNum)
                                    } else {
                                        menu.apply { //실패시 되돌려 놓기
                                            is_break = if (is_break == 0) {
                                                1
                                            } else {
                                                0
                                            }
                                        }
                                    }

                                } else {
                                    menu.apply {//실패시 되돌려 놓기
                                        is_break = if (is_break == 0) {
                                            1
                                        } else {
                                            0
                                        }
                                    }
                                }


                            }


                            true
                        }

                        R.id.menu_item_remove -> {//메뉴 삭제하기

                            viewLifecycleOwner.lifecycleScope.launch {
                                val response = viewModel.deleteMenu(menu.regNumber, menu.post_uuid)

                                if (response.isSuccessful) {
                                    viewModel.fetchRestaurantInfoByRegNum(menu.regNumber)
                                    showToast(requireContext(), "삭제되었습니다")
                                } else {
                                    showToast(requireContext(), "네트워크 연결에 실패했습니다")
                                }
                            }

                            true
                        }

                        else -> false

                    }

                }
                if (Build.VERSION.SDK_INT >= 23) {
                    gravity = Gravity.END
                }
                show()
            }

            true
        }


        binding.rvMenu.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.lvEmpty.apply {
            sandboxAnimations()
            playAnimation()
        }

        binding.lvClick.apply {
            sandboxAnimations()
            playAnimation()
        }

        viewModel.menuListLiveData.observe(viewLifecycleOwner) { menuList ->
            menuList?.let {
                menuAdapter.submitList(it)

//                if(it.isEmpty()) {
//                    val lottieView = LottieAnimationView(requireContext())
//                    lottieView.apply {
//                        setAnimation("lottie/empty.json")
//                        this.
//                        sandboxAnimations()
//                        playAnimation()
//
//                    }
//                    binding.flRv.addView(lottieView)
//
//                }

                binding.lvEmpty.visibility = if (it.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {

            val response = viewModel.fetchRestaurantInfoByRegNum(args.regNum)

            if (!response) {
                showToast(requireContext(), "메뉴를 수정할 수 없습니다")
                findNavController().popBackStack()
            }


        }

        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.getStoreInfoByRegNum(args.regNum)

            if (response.isSuccessful) {
                response.body()?.get(0)?.let { restaurantResponse->

                    restaurantResponse.market?.let {
                        Glide.with(requireContext())
                            .load(it.photoUrl)
                            .into(binding.ivRestaurantImage)

                        binding.tvRestaurantNameCategory.text = "${it.name} [${it.category}]"
                    }

                    restaurantResponse.menuList?.let {

                        binding.tvMenuCount.text = "등록한 메뉴: ${it.size}개"
                    }

                }
            } else {
                showToast(requireContext(), "네트워크 연결에 실패했습니다")
                findNavController().popBackStack()
            }

        }


        showTooltip(
            requireContext(),
            binding.tvMenuTitle,
            "메뉴를 꾹 눌러 관리해보세요 !",
            viewLifecycleOwner,
            arrowOrientation = ArrowOrientation.END
        )
    }

    private fun initToolbarView() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}