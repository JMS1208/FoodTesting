package com.capstone.foodtesting.ui.common.info

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.databinding.FragmentCommonManageBinding
import com.capstone.foodtesting.ui.bottomsheet.setaddress.BSSetupAddrFragment
import com.capstone.foodtesting.ui.common.info.favorite.FavoriteDialogFragment
import com.capstone.foodtesting.ui.restaurant.dialog.SelectMenuManageDialogFragment
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.Constants
import com.capstone.foodtesting.util.sandboxAnimations
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.SpinnerGravity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CommonManageFragment : Fragment() {

    private var _binding: FragmentCommonManageBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CommonManageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var memberInfo: Member? = null

    private var marketRegNum: String? = null

    private var restaurantResponseList: List<RestaurantResponse>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lottieSetup()


        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestAddressInfo.collectLatest { addressInfo ->
                    binding.tvAddress.text = addressInfo?.address?.addressFullName ?: "주소 설정하러 가기"
                }

            }
        }

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }


        binding.tvAddress.setOnClickListener {
            val bottomSheet = BSSetupAddrFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.tvLogout.setOnClickListener {
            viewModel.deleteMemeberInfo()
            Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentCommonLogin()
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMemberInfo.collect { member ->
                member?.let {
                    this@CommonManageFragment.memberInfo = it

                    val nickAndType =
                        "${it.nickName} [${if (it.type == Member.TYPE_TESTER) "테스터" else "사장님"}]"
                    binding.tvName.text = it.name ?: ""
                    binding.tvNicknameType.text = nickAndType
                    binding.tvEmail.text = it.email


                    binding.lvProfile.apply {
                        if (it.gender == Member.FEMALE) {
                            setAnimation("lottie/girl.json")
                            playAnimation()
                        }
                    }

                    if (it.type == Member.TYPE_CEO) {
                        val result = viewModel.getStoreInfoByCustomerId(it.uuid.toString())
                        if (result.isSuccessful) {
                            result.body()?.let { restaurantResponseList ->
                                this@CommonManageFragment.restaurantResponseList = restaurantResponseList
                                setupRestaurantSpinner()
                            }
                        }
                    } else {
                        binding.spinnerRestaurant.isClickable = false
                    }
                }


            }

        }

        binding.tvQrGenerate.setOnClickListener {
            memberInfo?.let {
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToFragmentCommonCodeGenerate(
                        it.uuid
                    )
                findNavController().navigate(action)


            }

        }
        binding.ivModifyProfile.setOnClickListener {
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentMemberInfoModify()
            findNavController().navigate(action)
        }
//        binding.tvLogOut.setOnClickListener {
//            val builder=AlertDialog.Builder(requireContext())
//            builder.setTitle("Alert")
//            builder.setMessage("로그아웃 하시겠습니까?")
//            builder.setNegativeButton("NO"){dialog,which->
//
//            }
//            builder.setPositiveButton("YES"){dialog,which->
//                viewModel.deleteMemeberInfo()
//                Toast.makeText(requireContext(),"로그아웃 되었습니다",Toast.LENGTH_SHORT).show()
//                viewLifecycleOwner.lifecycleScope.launch {
//                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
//                        viewModel.saveLogInState(LogInStateOptions.LOGGED_OUT.value)
//                    }
//                }
//                findNavController().popBackStack()
//            }
//            builder.show()
//        }
//        binding.ivQrTooltip.setOnClickListener {
//            showTooltip(requireContext(), it, "테스터가 리뷰 작성을 위해 필요한 QR 코드입니다\n캡처하여 매장에 비치해주세요", viewLifecycleOwner)
//        }


        binding.tvReviewCheck.setOnClickListener {

        }
        binding.tvRegisterRestaurant.setOnClickListener {
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantRegister()
            findNavController().navigate(action)
        }

        binding.tvMenuManage.setOnClickListener {

            val bottomSheetDialog = SelectMenuManageDialogFragment()

            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)

        }

        binding.tvRestaurantInfo.setOnClickListener {

            memberInfo?.let {

                viewLifecycleOwner.lifecycleScope.launch {
                    val result = viewModel.getStoreInfoByCustomerId(it.uuid.toString())

                    if (result.isSuccessful) {
                        val restaurant = result.body()?.get(0)?.market

                        restaurant?.let {
                            val action =
                                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantInfoModify(
                                    restaurant
                                )
                            findNavController().navigate(action)

                        }
                    } else {
                        Toast.makeText(requireContext(), "매장 입점 후 사용해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


            }


        }


        binding.tvQuestionManage.setOnClickListener {
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantQueryRegister()
            findNavController().navigate(action)
        }


        binding.tvFavorite.setOnClickListener {
            val favoriteDialogFragment = FavoriteDialogFragment()
            favoriteDialogFragment.show(childFragmentManager, favoriteDialogFragment.tag)

        }

        binding.tvQrScan.setOnClickListener {
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentCommonCodeScan()
            findNavController().navigate(action)
        }


        setupFragmentResultListener()

    }

    private fun setupRestaurantSpinner() {
        if(restaurantResponseList != null) {
            binding.spinnerRestaurant.apply {
//                setOnClickListener {
//                    val idx = binding.spinnerRestaurant.selectedIndex
//                    val response = restaurantResponseList!![idx]
//
//                    response.market?.reg_num?.let {
//                        marketRegNum = it
//                        val action =
//                            CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantRoom()
//                        findNavController().navigate(action)
//                    }
//                    showToast(requireContext(), "테스트1")
//                    this.clearSelectedItem()
//                }



                setSpinnerAdapter(IconSpinnerAdapter(this))

                arrowAnimate = true
                arrowGravity = SpinnerGravity.END
                setOnSpinnerOutsideTouchListener { _, _ ->
                    this.dismiss()
                }
                var typeface: Typeface? = null
                activity?.assets?.let {
                    typeface = Typeface.createFromAsset(it, "fontasset/noto_sans_kr_regular.otf")
                }

                val itemList = arrayListOf<IconSpinnerItem>()

                restaurantResponseList!!.forEach { restaurantResponse ->
                    restaurantResponse.market?.let { restaurant ->
                        val name = restaurant.name.toString()
                        val category = restaurant.category.toString()
                        val item = IconSpinnerItem(
                            text = "$name ($category)",
                            textTypeface = typeface,
                            gravity = Gravity.CENTER
                        )
                        itemList.add(item)
                    }
                }

                setItems(itemList)
                getSpinnerRecyclerView().layoutManager = LinearLayoutManager(requireContext())
                selectItemByIndex(0) // select a default item.
                marketRegNum = restaurantResponseList!![0].market?.reg_num
                lifecycleOwner = viewLifecycleOwner

                setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, oldItem, newIndex, newItem ->
                   val response = restaurantResponseList!![newIndex]

                    response.market?.reg_num?.let {
                        marketRegNum = it
                        val action =
                            CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantRoom(it)
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }

    private fun lottieSetup() {
        binding.lvAddress.apply {
            sandboxAnimations()
            playAnimation()
        }
        binding.lvRestaurantManage.apply {
            sandboxAnimations()
            playAnimation()
        }
        binding.lvMyActivity.apply {
            sandboxAnimations()
            playAnimation()
        }

        binding.lvProfile.apply {
            sandboxAnimations()
            playAnimation()
        }
    }

    private fun setupFragmentResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            Constants.CODE_SCAN_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->

            //정보를 꺼내와서
            val result: String? = bundle.getString(Constants.CODE_SCAN_BUNDLE_KEY)

            result?.let {
                //TODO 일단은 매장명 전달하는걸로 함 나중에 바꿀것 Home도 똑같이 해야하는거 잊지말기
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToFragmentMemberReview(
                        it
                    )
                findNavController().navigate(action)

            }
        }
        setFragmentResultListener("addMenu") { _, _ ->
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantAddMenu()
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}