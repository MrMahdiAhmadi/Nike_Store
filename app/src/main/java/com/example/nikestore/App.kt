package com.example.nikestore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.nikestore.data.db.AppDatabase
import com.example.nikestore.data.repo.*
import com.example.nikestore.data.repo.order.OrderRemoteDataSource
import com.example.nikestore.data.repo.order.OrderRepository
import com.example.nikestore.data.repo.order.OrderRepositoryImpl
import com.example.nikestore.data.repo.source.*
import com.example.nikestore.feature.auth.AuthViewModel
import com.example.nikestore.feature.cart.CartViewModel
import com.example.nikestore.feature.checkout.CheckoutViewModel
import com.example.nikestore.feature.common.ProductListAdapter
import com.example.nikestore.feature.favorite.FavoriteProductViewModel
import com.example.nikestore.feature.home.HomeViewModel
import com.example.nikestore.feature.list.ProductListViewModel
import com.example.nikestore.feature.main.MainViewModel
import com.example.nikestore.feature.main.detail.ProductDetailViewModel
import com.example.nikestore.feature.main.detail.comment.CommentListViewModel
import com.example.nikestore.feature.order.OrderHistoryViewModel
import com.example.nikestore.feature.profile.ProfileViewModel
import com.example.nikestore.feature.shipping.ShippingViewModel
import com.example.nikestore.services.FrescoImageLoadingService
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)

        val myModules = module {
            single { createApiServiceInstance() }
            single { Room.databaseBuilder(this@App,AppDatabase::class.java,"db_nike").build() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDatabase>().productDao()
                )
            }
            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            single { UserLocalDataSource(get()) }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }

            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            factory { (viewType: Int) -> ProductListAdapter(viewType, get()) }
            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }
            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get()) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckoutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductViewModel(get()) }
            viewModel { OrderHistoryViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}