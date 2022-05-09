package com.example.nikestore.data.repo.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductLocalDataSource : ProductDataSource {

    override fun getProducts(sort: Int): Single<List<Product>>{
        TODO()
    }

    @Query("SELECT * FROM products")
    override fun getFavoriteProducts(): Single<List<Product>>

    @Insert(onConflict = REPLACE)
    override fun addToFavorites(product: Product): Completable

    @Delete
    override fun deleteFromFavorite(product: Product): Completable
}