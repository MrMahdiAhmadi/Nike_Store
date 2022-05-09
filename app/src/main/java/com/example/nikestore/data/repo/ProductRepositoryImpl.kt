package com.example.nikestore.data.repo

import com.example.nikestore.data.repo.source.ProductDataSource
import com.example.nikestore.data.repo.source.ProductLocalDataSource
import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    val remoteDataSource: ProductDataSource,
    val localDataSource: ProductLocalDataSource
) : ProductRepository {

    override fun getProducts(sort: Int): Single<List<Product>> =
        localDataSource.getFavoriteProducts().flatMap { favProducts ->
            remoteDataSource.getProducts(sort).doOnSuccess {
                val favProductsId = favProducts.map {
                    it.id
                }
                it.forEach { product ->
                    if (favProductsId.contains(product.id))
                        product.isFav = true
                }
            }
        }

    override fun getFavoriteProducts(): Single<List<Product>> =
        localDataSource.getFavoriteProducts()

    override fun addToFavorites(product: Product): Completable =
        localDataSource.addToFavorites(product)

    override fun deleteFromFavorite(product: Product): Completable =
        localDataSource.deleteFromFavorite(product)
}