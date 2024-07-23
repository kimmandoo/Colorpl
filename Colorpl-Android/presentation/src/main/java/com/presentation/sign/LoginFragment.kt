package com.presentation.sign

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.BuildConfig
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.presentation.MainActivity
import com.presentation.base.BaseFragment
import com.presentation.util.setPasswordTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {


    private lateinit var googleIdOption: GetGoogleIdOption

    private lateinit var googleRequest: GetCredentialRequest

    private lateinit var credentialManager: CredentialManager


    override fun initView() {
        initGoogleLogin()
        initIncludeView()
        initClickEvent()
    }

    private fun initGoogleLogin() {
        googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_KEY)
            .setAutoSelectEnabled(true)
            .build()

        googleRequest =
            GetCredentialRequest.Builder().addCredentialOption(
                googleIdOption
            ).build()

        credentialManager = CredentialManager.create(requireActivity())
    }

    private fun initIncludeView() {
        binding.includePassword.etContent.apply {
            transformationMethod = PasswordTransformationMethod.getInstance()
            imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private fun initClickEvent() {
        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
            tvSignUp.setOnClickListener {
                navigateDestination(
                    findNavController(),
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

    private fun login() {
        viewLifecycleOwner.lifecycleScope.launch {
            kotlin.runCatching {
                credentialManager.getCredential(
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
        // Handle the successfully returned credential.
        val auth = Firebase.auth
        val credential = result.credential
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val idToken = googleIdTokenCredential.idToken
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
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