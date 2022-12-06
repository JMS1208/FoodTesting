package com.capstone.foodtesting.ui.common.manage

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.databinding.FragmentCommonManageBinding
import com.capstone.foodtesting.ui.bottomsheet.setaddress.BSSetupAddrFragment
import com.capstone.foodtesting.ui.common.manage.favorite.FavoriteDialogFragment
import com.capstone.foodtesting.ui.restaurant.dialog.SelectMenuManageDialogFragment
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.Constants
import com.capstone.foodtesting.util.sandboxAnimations
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.SpinnerGravity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Integer.min

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

    private var addressInfo: AddressInfo? = null

    private var marketRegNum: String? = null

    private var restaurantResponseList: List<RestaurantResponse>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbarView()

        setupClickListener() // 클릭 리스너 설정

        lottieSetup() //로티뷰 설정

        setupObserver() //옵저버들 설정

        setupFragmentResultListener() //프래그먼트 결과 리스너


    }


    private fun initToolbarView() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun updateUIAddressInfo(addressInfo: AddressInfo) {
        binding.tvAddress.text = addressInfo.address?.addressFullName ?: "주소 설정하러 가기"
    }

    private fun updateUIMemberInfo(memberInfo: Member) {


        val nickAndType =
            "${memberInfo.nickName} [${if (memberInfo.type == Member.TYPE_TESTER) "테스터" else "사장님"}]"
        binding.tvName.text = memberInfo.name ?: ""
        binding.tvNicknameType.text = nickAndType
        binding.tvEmail.text = memberInfo.email


        binding.lvProfile.apply {
            if (memberInfo.gender == Member.FEMALE) {
                setAnimation("lottie/girl.json")
                playAnimation()
            }
        }

        if (memberInfo.type == Member.TYPE_CEO) {

            setupRestaurantInfo(memberInfo)

        } else {
            binding.spinnerRestaurant.isClickable = false
        }
    }

    private fun setupRestaurantInfo(memberInfo: Member) = viewLifecycleOwner.lifecycleScope.launch {

        val response = viewModel.getStoreInfoByCustomerId(memberInfo.uuid.toString())
        if (response.isSuccessful) {
            response.body()?.let { restaurantResponseList ->
                if (restaurantResponseList.isNotEmpty()) {
                    this@CommonManageFragment.restaurantResponseList = restaurantResponseList
                    setupRestaurantSpinner(restaurantResponseList)
                }
            }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestAddressInfo.collectLatest { addressInfo ->
                    addressInfo?.let {
                        updateDataAddressInfo(it) //프래그먼트의 프로퍼티 설정
                        updateUIAddressInfo(it) // UI에 데이터 설정
                    }
                }
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMemberInfo.collect { member ->
                member?.let {
                    updateDataMemberInfo(it) //프래그먼트의 프로퍼티 설정
                    updateUIMemberInfo(it) // UI에 데이터 설정
                }
            }

        }
    }

    private fun updateDataMemberInfo(memberInfo: Member) {
        this.memberInfo = memberInfo
    }

    private fun updateDataAddressInfo(addressInfo: AddressInfo) {
        this.addressInfo = addressInfo
    }

    private fun setupClickListener() {

        binding.toolbar.apply { //툴바 세팅
            setNavigationIcon(R.drawable.ic_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.tvMyReviews.setOnClickListener {
            //내가쓴 리뷰 가져오기
            memberInfo?.let {
                val action = CommonManageFragmentDirections.actionFragmentCommonManageToReviewRecordsFragment(it)
                findNavController().navigate(action)
            }

        }

        binding.tvAddress.setOnClickListener {
            //주소 설정하는 바텀시트 띄우기
            showAddressSettingBottomSheet()
        }

        binding.tvLogout.setOnClickListener {
            //로그아웃 요청
            requestLogOut()

        }
        binding.tvQuestionManage.setOnClickListener {
            //매장 질문지 관리
            manageQuestionnaire()

        }


        binding.tvFavorite.setOnClickListener {
            //즐겨찾기 다이얼로그 띄우기
            showFavoriteDialog()

        }

        binding.tvQrScan.setOnClickListener {

            //큐알 스캔화면 이동
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentCommonCodeScan()
            findNavController().navigate(action)
        }

        binding.tvQrGenerate.setOnClickListener {
            marketRegNum?.let {
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToFragmentCommonCodeGenerate(
                        it
                    )
                findNavController().navigate(action)
            } ?: showToast(requireContext(), "등록된 매장이 없습니다")

        }
        binding.ivModifyProfile.setOnClickListener {

            memberInfo?.let {
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToFragmentMemberInfoModify(it)
                findNavController().navigate(action)
            } ?: showToast(requireContext(), "다시 로그인해주세요")

        }


        binding.tvReviewCheck.setOnClickListener {
            marketRegNum?.let {
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToFragmentReviewAnal(it)
                findNavController().navigate(action)
            } ?: showToast(requireContext(), "등록된 매장이 없습니다")
        }
        binding.tvRegisterRestaurant.setOnClickListener {
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantRegister()
            findNavController().navigate(action)
        }

        binding.tvMenuManage.setOnClickListener {

            //메뉴 관리할때 다이얼로그 띄워서 선택하게 하는거
            if(marketRegNum == null) {
                showToast(requireContext(), "등록된 매장이 없습니다")
            } else {
                showMenuManageDialog()
            }


        }


        binding.tvRestaurantInfo.setOnClickListener {

            marketRegNum?.let {
                moveToModifyRestaurantInfo(it)
            }
        }

    }

    private fun moveToModifyRestaurantInfo(regNum: String) =
        viewLifecycleOwner.lifecycleScope.launch {
            val result = withContext(viewLifecycleOwner.lifecycleScope.coroutineContext) {
                val response = viewModel.getStoreInfoByRegNum(regNum)

                if (response.isSuccessful) {
                    val restaurant = response.body()?.get(0)?.market

                    restaurant?.let {
                        val action =
                            CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantInfoModify(
                                it
                            )
                        findNavController().navigate(action)
                    }
                    true
                } else {
                    false
                }
            }

            if (!result) {
                showToast(requireContext(), "매장 입점 후 사용해주세요")
            }
        }


    private fun showMenuManageDialog() {
        val bottomSheetDialog = SelectMenuManageDialogFragment()
        bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
    }

    private fun showFavoriteDialog() {
        val favoriteDialogFragment = FavoriteDialogFragment()
        favoriteDialogFragment.show(childFragmentManager, favoriteDialogFragment.tag)
    }

    private fun manageQuestionnaire() {
        marketRegNum?.let {
            val action =
                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantQueryRegister(
                    it
                )
            findNavController().navigate(action)
        } ?: showToast(requireContext(), "매장 입점 후 이용하실 수 있어요")
    }

    private fun requestLogOut() {
        //기존데이터 삭제하고
        viewModel.deleteMemberInfo()

        showToast(requireContext(), "로그아웃 되었습니다")
        //TODO {원래는 아래처럼 이렇게 하면 안 되는게 뒤로가기 했을때 다시 돌아올 수 있어서}
        val action =
            CommonManageFragmentDirections.actionFragmentCommonManageToFragmentCommonLogin()
        findNavController().navigate(action)
    }

    private fun showAddressSettingBottomSheet() {
        val bottomSheet = BSSetupAddrFragment()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


    private fun setupRestaurantSpinner(restaurantResponseList: List<RestaurantResponse>) {


        binding.spinnerRestaurant.apply {
            lifecycleOwner = viewLifecycleOwner
            setSpinnerAdapter(IconSpinnerAdapter(this))
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(requireContext())
            arrowAnimate = true
            arrowGravity = SpinnerGravity.END
            setOnSpinnerOutsideTouchListener { _, _ ->
                this.dismiss()
            }

            val itemList = makeIconSpinnerItem(restaurantResponseList)
            setItems(itemList)

            val idx = min(viewModel.spinnerIdx, restaurantResponseList.lastIndex)

            setupInitSpinnerIdx(restaurantResponseList, idx)


            setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, oldItem, newIndex, newItem ->

                viewModel.spinnerIdx = newIndex

                this@CommonManageFragment.restaurantResponseList?.let { restaurantResponseList ->

                    setupSpinnerIdx(restaurantResponseList, newIndex)

                    if (oldIndex == newIndex) {
                        marketRegNum?.let {
                            val action =
                                CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantRoom(
                                    it
                                )
                            findNavController().navigate(action)
                        }
                    }
                }

            }
        }


    }

    private fun setupInitSpinnerIdx(restaurantResponseList: List<RestaurantResponse>, idx: Int) {
        restaurantResponseList[idx].market?.let {
            binding.spinnerRestaurant.selectItemByIndex(idx) // select a default item.
            marketRegNum = it.reg_num
        }
    }

    private fun setupSpinnerIdx(restaurantResponseList: List<RestaurantResponse>, idx: Int) {
        restaurantResponseList[idx].market?.let {
            marketRegNum = it.reg_num
        }
    }

    private fun makeIconSpinnerItem(restaurantResponseList: List<RestaurantResponse>): ArrayList<IconSpinnerItem> {
        var typeface: Typeface? = null

        activity?.assets?.let {
            typeface = Typeface.createFromAsset(it, "fontasset/noto_sans_kr_regular.otf")
        }

        val itemList = arrayListOf<IconSpinnerItem>()

        restaurantResponseList.map { restaurantResponse ->
            val restaurant = restaurantResponse.market

            restaurant?.let {
                val name = it.name.toString()
                val category = it.category.toString()
                val item = IconSpinnerItem(
                    text = "$name ($category)",
                    textTypeface = typeface,
                    gravity = Gravity.CENTER
                )
                itemList.add(item)
            }

        }

        return itemList

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

            result?.let { regNum ->
                memberInfo?.let {
                    val action =
                        CommonManageFragmentDirections.actionFragmentCommonManageToFragmentMemberReview(
                            regNum,
                            it
                        )
                    findNavController().navigate(action)
                }

            }
        }
        setFragmentResultListener(
            "modifyMenu"
        ) { _, _ ->

            try {
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToRestaurantModifyMenuFragment(
                        marketRegNum!!
                    )
                findNavController().navigate(action)

            } catch (E: Exception) {
                showToast(requireContext(), "입점된 매장이 없습니다")
            }

        }

        setFragmentResultListener("addMenu") { _, _ ->

            try {
                val action =
                    CommonManageFragmentDirections.actionFragmentCommonManageToFragmentRestaurantAddMenu(
                        marketRegNum!!,
                        memberInfo!!.uuid.toString()
                    )
                findNavController().navigate(action)
            } catch (E: Exception) {
                showToast(requireContext(), "입점된 매장이 없습니다")
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}