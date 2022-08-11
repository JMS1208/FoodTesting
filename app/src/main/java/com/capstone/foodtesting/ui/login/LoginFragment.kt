package com.capstone.foodtesting.ui.login

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R

import com.capstone.foodtesting.databinding.FragmentLoginBinding
import com.capstone.foodtesting.ui.register.RegisterFinishedFragmentDirections
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth=FirebaseAuth.getInstance()

        // Configure Google SignIn
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("106449633055-13ql64jaephpeemdveuo293q6b98do4n.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(requireContext(),gso)
        binding.btnLoginGoogle.setOnClickListener {
            signIn()
        }
        // Kakao Login
        KakaoSdk.init(requireContext(),"6ac939611554342426f6ee480a13f8ec")
        binding.btnLoginKakao.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(requireContext())){
                LoginClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
            }else{
                LoginClient.instance.loginWithKakaoAccount(requireContext(),callback=callback)
            }
        }
        // Naver Login Module Initialize
        val naverClientId=getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret=getString(R.string.social_login_info_naver_client_secret)
        val naverClientName=getString(R.string.social_login_info_naver_client_name)
        NaverIdLoginSDK.initialize(requireContext(),naverClientId,naverClientSecret,naverClientName)
        binding.btnLoginNaver.setOnClickListener {
            startNaverLogin()
        }
        return binding.root
    }

    private fun signIn() {
        val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }
    companion object{
        const val RC_SIGN_IN=1001
        const val EXTRA_NAME="EXTRA NAME"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 구글 로그인
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Google_SignIn", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google_SignIn", "Google sign in failed", e)
                // ...
            }
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("Google_SignIn", "signInWithCredential:success")
                    val user = auth.currentUser

                    // get User Information(Log)
                    Log.d("GoogleUser","${user!!.displayName}, ${user!!.email}, ${user!!.photoUrl.toString()}")

                    // saveData (ViewModel)
                    // Google: name, email, photoURL만 지원
                    viewModel.name= MutableLiveData(user!!.displayName)
                    viewModel.email=MutableLiveData(user!!.email)
                    viewModel.photoURL=MutableLiveData(user!!.photoUrl.toString())
                    viewModel.saveUserData()
                    updateUI(user)
                } else {
                    Log.w("Google_SignIn", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user:FirebaseUser?){
        if (user!=null){
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentHome()
            findNavController().navigate(action)
        }
    }
    val callback:(OAuthToken?,Throwable?)->Unit={token,error->
        if (error!=null){
            when{
                error.toString()== AuthErrorCause.AccessDenied.toString()->{
                    Toast.makeText(context,"접근이 거부됨(동의 취소)",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.InvalidClient.toString()->{
                    Toast.makeText(context,"유효하지 않은 앱",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.InvalidGrant.toString()->{
                    Toast.makeText(context,"인증수단이 유효하지 않아 인증할 수 없는 상태",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.InvalidRequest.toString()->{
                    Toast.makeText(context,"요청 파라미터 오류",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.InvalidScope.toString()->{
                    Toast.makeText(context,"유효하지 않은 scope ID",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.Misconfigured.toString()->{
                    Toast.makeText(context,"설정이 올바르지 않음(android key hash)",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.ServerError.toString()->{
                    Toast.makeText(context,"서버 네부 에러",Toast.LENGTH_SHORT).show()
                }
                error.toString()== AuthErrorCause.Unauthorized.toString()->{
                    Toast.makeText(context,"앱이 요청 권한이 없음",Toast.LENGTH_SHORT).show()
                }
                else->{
                    Toast.makeText(context,"기타 에러",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (token!=null){
            Toast.makeText(context,"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show()
            UserApiClient.instance.me { user, error ->
                // get user info
                Log.d("KakaoUser","${user?.kakaoAccount?.profile?.nickname}\n${user?.kakaoAccount?.gender}\n${user?.kakaoAccount?.ageRange}\n${user?.kakaoAccount?.birthday}\n${user?.kakaoAccount?.birthyear}")

                // saveData(ViewModel)
                // KakaoAccount: name, email, photoURL(필수동의) / gender, birthDay, ageRange(선택동의)
                viewModel.name= MutableLiveData(user?.kakaoAccount?.profile?.nickname)
                viewModel.email=MutableLiveData(user?.kakaoAccount?.email)
                viewModel.gender=MutableLiveData(user?.kakaoAccount?.gender.toString())
                //viewModel.age= MutableLiveData(user?.kakaoAccount?.ageRange.toString())
                viewModel.birthDay=MutableLiveData(user?.kakaoAccount?.birthday)
                viewModel.photoURL=MutableLiveData(user?.kakaoAccount?.profile?.profileImageUrl)
                viewModel.saveUserData()
            }
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentHome()
            findNavController().navigate(action)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionFragmentLoginToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener { //테스트용
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentHome()
            findNavController().navigate(action)
        }

    }

    private fun startNaverLogin(){
        var naverToken:String?=""

        val profileCallback=object:NidProfileCallback<NidProfileResponse>{
            override fun onSuccess(response: NidProfileResponse) {
                // get user info
                Log.d("NaverUser","${response.profile?.age}\n${response.profile?.nickname}\n${response.profile?.gender}\n${response.profile?.birthday}\n${response.profile?.birthYear}\n${response.profile?.email}")

                // saveData(ViewModel)
                viewModel.name= MutableLiveData(response.profile?.name)
                viewModel.email=MutableLiveData(response.profile?.email)
                viewModel.gender= MutableLiveData(response.profile?.gender)
                viewModel.birthYear= MutableLiveData(response.profile?.birthYear)
                viewModel.birthDay=MutableLiveData(response.profile?.birthday)
                viewModel.photoURL=MutableLiveData(response.profile?.profileImage)
                viewModel.saveUserData()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode=NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription=NaverIdLoginSDK.getLastErrorDescription()
                Log.d("Naver_Login","errorCode:${errorCode}\nerrorDescription: ${errorDescription}")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode,message)
            }
        }

        val oauthLoginCallback=object:OAuthLoginCallback{
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken=NaverIdLoginSDK.getAccessToken()
                // 로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
                val action = LoginFragmentDirections.actionFragmentLoginToFragmentHome()
                findNavController().navigate(action)

            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode=NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription=NaverIdLoginSDK.getLastErrorDescription()
                Log.d("Naver_Login","errorCode:${errorCode}\nerrorDescription: ${errorDescription}")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode,message)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(),oauthLoginCallback)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}