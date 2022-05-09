package com.example.nikestore.feature.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.ProductRepository
import io.reactivex.schedulers.Schedulers

class FavoriteProductViewModel(private val productRepository: ProductRepository) : NikeViewModel() {

    val productLiveData = MutableLiveData<List<Product>>()

    init {
        productRepository.getFavoriteProducts()
            .subscribeOn(Schedulers.io())
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productLiveData.postValue(t)
                }
            })
    }

    fun removeFromFavorite(product: Product) {
        productRepository.deleteFromFavorite(product)
            .subscribeOn(Schedulers.io())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Log.i("TAG", "removeFromFavorite: ")
                }

            })
    }
}