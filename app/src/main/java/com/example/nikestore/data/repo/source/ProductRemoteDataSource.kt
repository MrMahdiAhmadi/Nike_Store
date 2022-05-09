package com.example.nikestore.data.repo.source

import com.example.nikestore.services.http.ApiService
import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(val apiService: ApiService) : ProductDataSource {

    override fun getProducts(sort: Int): Single<List<Product>> =
        apiService.getProductList(sort.toString())

    override fun getFavoriteProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorite(product: Product): Completable {
        TODO("Not yet implemented")
    }
}