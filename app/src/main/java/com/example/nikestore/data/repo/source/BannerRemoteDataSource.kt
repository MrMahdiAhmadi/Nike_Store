package com.example.nikestore.data.repo.source

import com.example.nikestore.services.http.ApiService
import com.example.nikestore.data.Banner
import io.reactivex.Single

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {

    override fun getBanners(): Single<List<Banner>> = apiService.getBanners()
}