package com.presentation.sign

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentLoginBinding
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.presentation.MainActivity
import com.presentation.base.BaseFragment
import com.presentation.component.dialog.LoadingDialog
import com.presentation.sign.model.SignInEventState
import com.presentation.util.setPasswordTransformation
import com.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    @Inject
    lateinit var googleIdOption: GetSignInWithGoogleOption

    private lateinit var googleRequest: GetCredentialRequest

    private val loginViewModel: LoginViewModel by viewModels()

    val loading by lazy {
        LoadingDialog(requireActivity())
    }


    override fun initView() {
        initGoogleLogin()
        initIncludeView()
        initClickEvent()
        observeLoginSuccess()
    }

    private fun initGoogleLogin() {
        googleRequest =
            GetCredentialRequest.Builder().addCredentialOption(
                googleIdOption
            ).build()

    }

    private fun initIncludeView() {
        binding.apply {
            with(includePassword.etContent) {
                transformationMethod = PasswordTransformationMethod.getInstance()
                imeOptions = EditorInfo.IME_ACTION_DONE
            }
        }
    }

    private fun initClickEvent() {
        binding.apply {
            tvLogin.setOnClickListener { //로그인 버튼 클릭
                loginViewModel.signIn(
                    includeId.etContent.text.toString(),
                    includePassword.etContent.text.toString()
                )
            }
            tvSignUp.setOnClickListener {
                navigateDestination(
                    R.id.action_fragment_login_to_fragment_sign_up
                )
            }
            includePassword.ivPasswordToggle.setOnClickListener {
                includePassword.etContent.setPasswordTransformation()
            }
            clGoogleLogin.setOnClickListener {
                Timber.d("구글 로그인 클릭")
                login()
            }
        }
    }

    private fun observeLoginSuccess() {
        loading.show()
        loginViewModel.signInEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                loading.dismiss()
                when (it) {
                    is SignInEventState.SignInSuccess -> {
                        Timber.d("로그인 성공")
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                    }

                    is SignInEventState.Error -> {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is SignInEventState.NoSign -> {
                        Timber.d("로그인 막기")
                    }
                }

            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun login() {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                CredentialManager.create(requireActivity()).getCredential(
                    request = googleRequest,
                    context = requireActivity(),
                )
            }.onSuccess { result ->
                handleSignIn(result)
            }.onFailure { e ->
                Timber.d("구글 로그인 오류 확인 $e")
            }
        }

    }


    fun handleSignIn(result: GetCredentialResponse) {
        val auth = Firebase.auth
        val credential = result.credential
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    Timber.d("구글 로그인 타입처리")
                    val idToken = googleIdTokenCredential.idToken
                    Timber.d("구글 아이디 토큰 $idToken")
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loginViewModel.googleSignIn(idToken)
                                Timber.d("구글 로그인 성공")
                            } else {
                                Timber.d("구글 로그인 실패 ${task.exception}")
                            }
                        }
                }
            }
        }
    }
}
