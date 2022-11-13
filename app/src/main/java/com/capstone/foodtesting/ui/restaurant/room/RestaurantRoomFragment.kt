package com.capstone.foodtesting.ui.restaurant.room

import android.annotation.SuppressLint
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.EdgeEffectCompat.getDistance
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.kakao.local.Address
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.FragmentRestaurantRoomBinding
import com.capstone.foodtesting.ui.restaurant.room.adapter.MenuAdapter
import com.capstone.foodtesting.util.CommonFunc.createLottieView
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.Constants.InitLatitude
import com.capstone.foodtesting.util.Constants.InitLongitude
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class RestaurantRoomFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRestaurantRoomBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RestaurantRoomViewModel>()

    private val args by navArgs<RestaurantRoomFragmentArgs>()

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var naverMap: NaverMap

    private var restaurantInfo: Restaurant? = null

    private var isSavedFavoriteRestaurant: Boolean = false

    private var longitude: Double = 126.9570
    private var latitude: Double = 37.50415

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantRoomBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        activity?.let {
            val fm = childFragmentManager
            val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction()
                        .add(R.id.map_fragment, it)
                        .addToBackStack(null)
                        .commit()
                }
            mapFragment.getMapAsync(this)
            mapFragment?.mapView?.setOnTouchListener { v, m ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                v.onTouchEvent(m)
                true
            }

        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val result = viewModel.getStoreInfoByRegNum(args.regNum)

            if (result.isSuccessful) {
                val restaurant = result.body()?.get(0)?.market
                val menuList = result.body()?.get(0)?.menuList

                restaurant?.let {
                    setupRestaurantInfo(it)
                }

                menuList?.let {
                    setupMenuRecyclerView(it)
                }


            } else {
                findNavController().popBackStack()
                showToast(requireContext(), "현재 이용할 수 없습니다")
            }
        }



        binding.btnBookmark.setOnClickListener {
            //즐겨찾기
            restaurantInfo?.let {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

                    if (isSavedFavoriteRestaurant) {
                        viewModel.deleteFavoriteRestaurant(it)
                        isSavedFavoriteRestaurant = false
                        withContext(Dispatchers.Main) {
                            setupFavoriteBtn()
                        }
                    } else {
                        viewModel.insertFavoriteRestaurant(it)
                        isSavedFavoriteRestaurant = true
                        withContext(Dispatchers.Main) {
                            showLottieFavorite()
                            setupFavoriteBtn()
                        }
                    }


                }

            }

        }


    }

    private fun initFavoriteBtn() {
        restaurantInfo?.let {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val result = viewModel.doExistsFavoriteRestaurant(it)

                isSavedFavoriteRestaurant = result != null
                withContext(Dispatchers.Main) {
                    setupFavoriteBtn()
                }
            }

        }

    }

    private fun setupFavoriteBtn() {
        if (isSavedFavoriteRestaurant) {
            binding.btnBookmark.setBackgroundResource(R.drawable.bg_btn_gradient2_5)

        } else {
            binding.btnBookmark.setBackgroundResource(R.drawable.bg_btn_transparent_stroke_white2_5)

        }
    }

    private fun showLottieFavorite() {
        val lottieView =
            createLottieView(requireContext(), "lottie/heart.json")

        binding.parentLayout.addView(lottieView)

        lottieView.apply {
            repeatCount = 0
            speed = 2f
            playAnimation()
        }
    }

    private fun setupRestaurantInfo(restaurant: Restaurant) {
        restaurantInfo = restaurant
        Glide.with(requireContext())
            .load(restaurant.photoUrl)
            .into(binding.ivRestaurantImage)

        binding.tvRestaurantName.text = "\" ${restaurant.name} \""
        binding.tvRestaurantName2.text = restaurant.name
        binding.tvCategory.text = restaurant.category
        binding.tvComplexity.text = restaurant.complexity ?: "보통"
        binding.tvTelephone.text = restaurant.telephoneNumber ?: "연락처 없음"
        binding.tvAddress.text = restaurant.address ?: "지번 주소 없음"

        restaurant.roadAddress?.let {
            binding.tvRoadAddress.text = it
        } ?: run {
            binding.llRoadAddress.visibility = View.GONE
        }

        longitude = restaurant.longitude ?: 126.9570
        latitude = restaurant.latitude ?: 37.50415

        initFavoriteBtn()
        initDistanceText()

    }


    private fun initDistanceText() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLatestAddressInfo().collectLatest { addressInfo ->
                addressInfo?.address?.let {
                    calculateDistance(it)
                } ?: run {
                    binding.tvDistance.text = "-"
                }
            }
        }
    }

    private fun calculateDistance(address: Address) {
        viewLifecycleOwner.lifecycleScope.launch {
            val addressName = address.addressFullName

            addressName?.let {
                restaurantInfo?.let { restaurant->
                    val result = viewModel.searchGeoInfo(addressName, restaurant.longitude.toString(), restaurant.latitude.toString() )

                    if (result.isSuccessful) {
                        result.body()?.addresses?.get(0)?.distance?.let {
                            binding.tvDistance.text = "${it.toInt().toFloat()/1000} km"
                        } ?: run {
                            binding.tvDistance.text = "-"
                        }
                    } else {
                        binding.tvDistance.text = "-"
                    }
                }

            }

        }
    }


    private fun setupMenuRecyclerView(menuList: List<Menu>) {

        menuAdapter = MenuAdapter(menuList).apply {
            setOnItemClickListener { menu ->
                val action =
                    RestaurantRoomFragmentDirections.actionFragmentRestaurantRoomToMenuDetailDialogFragment(
                        menu
                    )
                findNavController().navigate(action)
            }
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


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(latitude, longitude), 16.0).animate(
            CameraAnimation.Easing
        )
        this.naverMap.moveCamera((cameraUpdate))
    }

}