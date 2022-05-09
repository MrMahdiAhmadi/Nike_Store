package com.example.nikestore.feature.home

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.repo.BannerRepository
import com.example.nikestore.data.repo.ProductRepository
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.data.Banner
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val productRepository: ProductRepository, bannerRepository: BannerRepository) :
    NikeViewModel() {

    val latestProductsLiveData = MutableLiveData<List<Product>>()
    val bannersLiveData = MutableLiveData<List<Banner>>()

    init {
        progressBarLiveData.value = true
        productRepository.getProducts(SORT_LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    latestProductsLiveData.value = t
                }
            })

        bannerRepository.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : NikeSingleObserver<List<Banner>>(compositeDisposable) {
                    override fun onSuccess(t: List<Banner>) {
                        bannersLiveData.value = t
                    }
                })
    }

    fun addProductToFav(product: Product){
        if(product.isFav) {
            productRepository.deleteFromFavorite(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFav = false
                    }
                })
        }else{
            productRepository.addToFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFav = true
                    }
                })
        }
    }
}