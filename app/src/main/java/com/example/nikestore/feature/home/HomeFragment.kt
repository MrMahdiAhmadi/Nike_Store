package com.example.nikestore.feature.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.feature.common.ProductListAdapter
import com.example.nikestore.feature.common.VIEW_TYPE_ROUND
import com.example.nikestore.feature.list.ProductListActivity
import com.example.nikestore.feature.main.BannerSliderAdapter
import com.example.nikestore.feature.main.detail.ProductDetailActivity
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : NikeFragment(), ProductListAdapter.ProductOnClickListener {

    private val homeViewModel: HomeViewModel by viewModel()
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        latestProductsRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        latestProductsRv.adapter = productListAdapter
        productListAdapter.productOnClickListener = this

        viewLatestProductBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }

        setProgressIndicator(true)
        homeViewModel.latestProductsLiveData.observe(viewLifecycleOwner) {
            Log.i("TAG", "onViewCreated: " + it.toString())
            productListAdapter.products = it as ArrayList<Product>
        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }


        homeViewModel.bannersLiveData.observe(viewLifecycleOwner) {
            Log.i("TAGG", "onViewCreated: " + it.toString())
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            bannerSliderViewPager.adapter = bannerSliderAdapter

            val viewPagerHeight = (((bannerSliderViewPager.measuredWidth - convertDpToPixel(
                32f, requireContext()
            )) * 173) / 328).toInt()

            val layoutParams = bannerSliderViewPager.layoutParams

            layoutParams.height = viewPagerHeight

            sliderIndicator.setViewPager2(bannerSliderViewPager)
        }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavBtnClick(product: Product) {
        homeViewModel.addProductToFav(product)
    }
}