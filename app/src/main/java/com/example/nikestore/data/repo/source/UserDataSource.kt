package com.example.nikestore.data.repo.source

import com.example.nikestore.data.TokenResponse
import com.example.nikestore.data.MessageResponse
import io.reactivex.Single

interface UserDataSource {

    fun login(username: String, password: String): Single<TokenResponse>

    fun singUp(username: String, password: String): Single<MessageResponse>

    fun loadToken()

    fun saveToken(token: String, refreshToken: String)

    fun saveUsername(username: String)
    fun getUsername(): String
    fun signOut()
}