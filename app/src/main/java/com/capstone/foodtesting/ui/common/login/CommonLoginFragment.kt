package com.capstone.foodtesting.ui.common.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.data.model.member.Member

import com.capstone.foodtesting.databinding.FragmentCommonLoginBinding
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.Constants.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CommonLoginFragment : Fragment() {

    private var _binding: FragmentCommonLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CommonLoginViewModel>()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 구글 로그인
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                // Log.d("Google_SignIn", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // Log.w("Google_SignIn", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //Log.d("Google_SignIn", "signInWithCredential:success")
                    val user = auth.currentUser

                    // get User Information(Log)
                    //Log.d("GoogleUser","${user!!.displayName}, ${user!!.email}, ${user!!.photoUrl.toString()}")

                    // saveData (ROOM)
                    // Google: name, email, photoURL만 지원

                    val member = Member(
                        name = user!!.displayName ?: "someone",
//                        age = 0,
                        birthDate = System.currentTimeMillis(),
                        email = user!!.email ?: "",
                        gender = Member.UNKNOWN,
                        nickName = user!!.displayName ?: "someone",
//                        phoneNumber = "",
//                        profile =user!!.photoUrl.toString(),
                        social_member = Member.GOOGLE_MEMBER,
                        password = null
                    )
                    /*
                    viewLifecycleOwner.lifecycleScope.launch{
                        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                            viewModel.saveLogInState(LogInStateOptions.GOOGLE_SOCIAL_LOGGED_IN.value)
                        }
                    }

                     */
                    viewModel.insertMember(member)
                    viewModel.saveLogInState(LogInStateOptions.GOOGLE_SOCIAL_LOGGED_IN.value)
                    updateUI(user)
                    // TODO("Send User Info to BE")

                } else {
                    //Log.w("Google_SignIn", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val action =
                CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
            findNavController().navigate(action)
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when(error.toString()) {
                AuthErrorCause.AccessDenied.toString() -> {
                    CommonFunc.showToast(requireContext(), "접근이 거부됨(동의 취소)")
                }
                AuthErrorCause.InvalidClient.toString() -> {
                    CommonFunc.showToast(requireContext(), "유효하지 않은 앱")
                }
                AuthErrorCause.InvalidGrant.toString() -> {
                    CommonFunc.showToast(requireContext(), "인증수단이 유효하지 않아 인증할 수 없는 상태")
                }
                AuthErrorCause.InvalidRequest.toString() -> {
                    CommonFunc.showToast(requireContext(), "요청 파라미터 오류")
                }
                AuthErrorCause.InvalidScope.toString() -> {
                    CommonFunc.showToast(requireContext(), "유효하지 않은 scope ID")
                }
                AuthErrorCause.Misconfigured.toString() -> {
                    CommonFunc.showToast(requireContext(), "설정이 올바르지 않음(android key hash)")

                }
                AuthErrorCause.ServerError.toString() -> {
                    CommonFunc.showToast(requireContext(), "서버 네부 에러")
                }
                AuthErrorCause.Unauthorized.toString() -> {
                    CommonFunc.showToast(requireContext(), "앱이 요청 권한이 없음")
                }
                else -> Unit
            }
        } else if (token != null) {
            CommonFunc.showToast(requireContext(), "로그인에 성공하였습니다.")
            UserApiClient.instance.me { user, error ->
                // get user info
                Log.d(
                    "KakaoUser",
                    "${user?.kakaoAccount?.profile?.nickname}\n${user?.kakaoAccount?.gender}\n${user?.kakaoAccount?.ageRange}\n${user?.kakaoAccount?.birthday}\n"
                )

                // saveData(ViewModel)
                // KakaoAccount: name, email, photoURL(필수동의) / gender, birthDay(MM-DD), ageRange(선택동의)
                val ageRangeArr = user?.kakaoAccount?.ageRange.toString().split("_")
                val userAge = ageRangeArr[1].toInt()
                val member = Member(
//                    age = userAge,
                    birthDate = System.currentTimeMillis(),
                    email = user?.kakaoAccount?.email ?: "",
                    gender = if (user?.kakaoAccount?.gender.toString() == "FEMALE") Member.FEMALE else Member.MALE,
                    name = user?.kakaoAccount?.profile?.nickname ?: "someone",
                    nickName = user?.kakaoAccount?.profile?.nickname ?: "someone",
//                    phoneNumber = null,
//                    profile =user?.kakaoAccount?.profile?.profileImageUrl,
                    social_member = Member.KAKAO_MEMBER,
                    password = null
                )

                /*
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.saveLogInState(LogInStateOptions.KAKAO_SOCIAL_LOGGED_IN.value)
                    }
                }

                 */
                viewModel.insertMember(member)
                viewModel.saveLogInState(LogInStateOptions.KAKAO_SOCIAL_LOGGED_IN.value)
                // TODO("Send User Info to BE")
            }
            val action =
                CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMember.collectLatest {
                    it?.let {
                        val action = CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
                        findNavController().navigate(action)
                    }
                }
            }
        }



        // TODO("로그인 상태 유지 버튼 활성화 & 로그인 값 !=null : 바로 홈 화면 넘어가기")
        // Configure Google SignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("106449633055-13ql64jaephpeemdveuo293q6b98do4n.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        binding.btnLoginGoogle.setOnClickListener {
            signIn()
        }
        // Kakao Login
        KakaoSdk.init(requireContext(), "6ac939611554342426f6ee480a13f8ec")
        binding.btnLoginKakao.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                LoginClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }

        // Naver Login Module Initialize
        val naverClientId = getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
        val naverClientName = getString(R.string.social_login_info_naver_client_name)
        NaverIdLoginSDK.initialize(
            requireContext(),
            naverClientId,
            naverClientSecret,
            naverClientName
        )
        binding.btnLoginNaver.setOnClickListener {
            startNaverLogin()
        }
        binding.tvRegister.setOnClickListener {
            val action =
                CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentMemberSignUp()
            findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener { //테스트용
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                CommonFunc.showToast(requireContext(), "이메일을 입력해주세요")
            } else if (password.isEmpty()) {
                CommonFunc.showToast(requireContext(), "비밀번호를 입력해주세요")
            } else {
                CoroutineScope(Dispatchers.Main).launch {

                    val result = viewModel.loginUser(email, password)

                    if (result.isSuccessful) {
                        //아래는 홈 화면 들어가는 코드
                        result.body()?.let {

                            viewModel.insertMember(it)
                            val action =
                                CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
                            // TODO("email,pw로 BE에서 정보 맞는지 확인")
                            findNavController().navigate(action)
                        } ?: CommonFunc.showToast(requireContext(), "유효하지 않은 회원입니다")



                    } else {
                        CommonFunc.showToast(requireContext(), "이메일과 비밀번호를 확인해주세요")

                    }
                }
            }


//            //아래는 홈 화면 들어가는 코드
//            val action = CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
//            // TODO("email,pw로 BE에서 정보 맞는지 확인")
//            findNavController().navigate(action)

            //아래는 글쓰기 화면 들어가는 코드
            //findNavController().navigate(R.id.fragment_write)


        }

        setFragmentResultListener("SignUp") { _, _ ->
            val action =
                CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentMemberSignUpCompleted()
            findNavController().navigate(action)
        }

        setFragmentResultListener("SignUpCompleted") { _, bundle ->
            val isLogin = bundle.getBoolean("login")

            if (isLogin) {
                val action =
                    CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
                findNavController().navigate(action)
            }
        }


    }


    private fun startNaverLogin() {
        var naverToken: String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                // get user info
                Log.d(
                    "NaverUser",
                    "${response.profile?.age}\n${response.profile?.nickname}\n${response.profile?.gender}\n${response.profile?.birthday}\n${response.profile?.birthYear}\n${response.profile?.email}"
                )

                // TODO("Send User Info to BE")
                val birthdayArr = response.profile?.birthday!!.split("-")
                val birthdayMonth = birthdayArr[0].toInt()
                val birthdayDate = birthdayArr[1].toInt()
                val member = Member(
//                    age = Date().year-response.profile?.birthYear!!.toInt()+1,
                    birthDate = Date(
                        response.profile?.birthYear!!.toInt(),
                        birthdayMonth,
                        birthdayDate
                    ).time,
                    email = response.profile?.email ?: "",
                    gender = if (response.profile?.gender == "F") Member.FEMALE else Member.MALE,
                    name = response.profile?.name ?: "someone",
                    nickName = response.profile?.name ?: "someone",
//                    phoneNumber = "",
//                    profile =response.profile?.profileImage,
                    social_member = Member.NAVER_MEMBER,
                    password = null

                )
                /*
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.saveLogInState(LogInStateOptions.NAVER_SOCIAL_LOGGED_IN.value)
                    }
                }
                */
                viewModel.saveLogInState(LogInStateOptions.NAVER_SOCIAL_LOGGED_IN.value)
                viewModel.insertMember(member)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.d(
                    "Naver_Login",
                    "errorCode:${errorCode}\nerrorDescription: ${errorDescription}"
                )
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
                // 로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
                val action =
                    CommonLoginFragmentDirections.actionFragmentCommonLoginToFragmentCommonHome()
                findNavController().navigate(action)

            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.d(
                    "Naver_Login",
                    "errorCode:${errorCode}\nerrorDescription: ${errorDescription}"
                )
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}