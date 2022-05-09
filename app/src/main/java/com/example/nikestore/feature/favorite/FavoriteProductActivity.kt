package com.example.nikestore.feature.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Product
import com.example.nikestore.feature.main.detail.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_favorite_product.*
import kotlinx.android.synthetic.main.view_cart_empty_state.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteProductActivity : NikeActivity(),
    FavoriteProductAdapter.FavoriteProductEventListener {

    val viewModel: FavoriteProductViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_product)

        viewModel.productLiveData.observe(this) {
            if (it.isNotEmpty()) {
                favoriteProductRv.layoutManager =
                    LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                favoriteProductRv.adapter =
                    FavoriteProductAdapter(it as MutableList<Product>, this, get())
            } else {
                showEmptyState(R.layout.view_default_empty_state)
                emptyStateMessageTv.text = getString(R.string.favorites_empty_state_message)
            }
        }
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        viewModel.removeFromFavorite(product)
    }
}