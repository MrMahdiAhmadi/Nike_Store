package com.example.nikestore.data.repo

import com.example.nikestore.data.repo.source.BannerDataSource
import com.example.nikestore.data.Banner
import io.reactivex.Single

class BannerRepositoryImpl(private val bannerRemoteDataSource: BannerDataSource) : BannerRepository {

    override fun getBanners(): Single<List<Banner>> = bannerRemoteDataSource.getBanners()
}