package com.example.nikestore.feature.list

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.R
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.repo.ProductRepository
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.data.Product
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(var sort: Int, val productRepository: ProductRepository) :
    NikeViewModel() {

    val productsLiveData = MutableLiveData<List<Product>>()
    val selectedSortTitleLiveData = MutableLiveData<Int>()
    val sortTitles = arrayOf(
        R.string.sortLatest,
        R.string.sortPopular,
        R.string.sortPriceHighToLow,
        R.string.sortPriceLowToHigh
    )

    init {
        getProducts()
        selectedSortTitleLiveData.value = sortTitles[sort]
    }

    fun getProducts() {
        progressBarLiveData.value = true
        productRepository.getProducts(sort)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productsLiveData.value = t
                }

            })
    }

    fun onSelectedSortChangeByUser(sort: Int) {
        this.sort = sort
        this.selectedSortTitleLiveData.value = sortTitles[sort]
        getProducts()
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