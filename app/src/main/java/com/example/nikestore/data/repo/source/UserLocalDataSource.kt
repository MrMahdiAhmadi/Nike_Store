package com.example.nikestore.data.repo.source

import android.content.SharedPreferences
import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.TokenResponse
import com.example.nikestore.data.MessageResponse
import io.reactivex.Single
import timber.log.Timber


class UserLocalDataSource(private val sharedPreferences: SharedPreferences) : UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun singUp(username: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        Timber.i(TokenContainer.token)
        TokenContainer.update(
            sharedPreferences.getString("accesss_token", null),
            sharedPreferences.getString("refreshh_token", null)
        )
        Timber.i(TokenContainer.token)
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply() {
            putString("accesss_token", token)
            putString("refreshh_token", refreshToken)
        }.apply()
    }

    override fun saveUsername(username: String) {
        sharedPreferences.edit().apply {
            putString("username", username)
        }.apply()
    }

    override fun getUsername(): String = sharedPreferences.getString("username", "") ?: ""
    override fun signOut() = sharedPreferences.edit().apply {
        clear()
    }.apply()
}