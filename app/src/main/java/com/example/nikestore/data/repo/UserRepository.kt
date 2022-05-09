package com.example.nikestore.data.repo

import io.reactivex.Completable

interface UserRepository {

    fun login(username:String,password:String):Completable

    fun singUp(username: String,password: String):Completable

    fun loadToken()

    fun getUsername():String

    fun signOut()
}