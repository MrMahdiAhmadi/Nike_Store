package com.example.nikestore.data.repo

import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.TokenResponse
import com.example.nikestore.data.repo.source.UserDataSource
import io.reactivex.Completable

class UserRepositoryImpl(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteDataSource: UserDataSource
) : UserRepository {

    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username,it)
            //برای اینکه سینگل به کامپلیت ایبل تبدیل بشه
        }.ignoreElement()
    }

    override fun singUp(username: String, password: String): Completable {
        //فلت مپ برای چین کردن چنتا ریکویست

        return userRemoteDataSource.singUp(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun getUsername(): String = userLocalDataSource.getUsername()
    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null, null)
    }

    private fun onSuccessfulLogin(username: String, tokenResponse: TokenResponse) {
        //همون اول برنامه متد لود توی لوکال کال میشه و توکن میره توی کانتینر
        //بعد هروقت ک لاگین بشه یا هرچی باید جدیده بیاد ک اینجوری باید بزاریمش
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        //قبلی موقته و برنامه بسته بشه میره ولی این پایینی توی شیرید پرفرنسس هم سیو میکنه واسه بعدا
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUsername(username)
    }
}