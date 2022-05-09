package com.example.nikestore.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nikestore.R
import com.example.nikestore.common.NikeCompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.ext.android.inject

class SignUpFragment : Fragment() {

    private val viewModel: AuthViewModel by inject()

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpBtn.setOnClickListener {
            viewModel.signUp(signUpEmailEd.text.toString(), signUpPasswordEd.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        requireActivity().finish()
                    }
                })
        }

        loginLinkBtn.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, SignUpFragment())
            }.commit()
        }

    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}