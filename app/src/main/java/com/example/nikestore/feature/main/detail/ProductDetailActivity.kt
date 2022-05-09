package com.example.nikestore.feature.main.detail

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.formatPrice
import com.example.nikestore.feature.main.detail.comment.CommentListActivity
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.scroll.ObservableScrollViewCallbacks
import com.example.nikestore.view.scroll.ScrollState
import com.example.nikestore.data.Comment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductDetailActivity : NikeActivity() {

    private val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    private val commentAdapter = CommentAdapter()

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productDetailViewModel.productLiveData.observe(this) {
            imageLoadingService.load(productDetailIv, it.image)
            productDetailTitleTv.text = it.title
            productDetailPreviousPriceTv.text = formatPrice(it.previous_price)
            productDetailPreviousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            productDetailCurrentPriceTv.text = formatPrice(it.price)
            productDetailToolbarTitleTv.text = it.title
        }

        initViews()

        productDetailViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        productDetailViewModel.commentLiveData.observe(this) {
            commentAdapter.comments = it as ArrayList<Comment>

            if (it.size > 3) productDetailViewAllComments.visibility = View.VISIBLE

            productDetailViewAllComments.setOnClickListener {
                startActivity(Intent(this, CommentListActivity::class.java).apply {
                    putExtra(EXTRA_KEY_ID, productDetailViewModel.productLiveData.value!!.id)
                })
            }
        }
    }

    private fun initViews() {

        commentRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        commentRv.adapter = commentAdapter

        productDetailIv.post {

            val productIvHeight = productDetailIv.height
            val toolbar = toolbarView
            val productImageView = productDetailIv

            observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {

                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {

                }
            })
        }

        productDetailAddToCard.setOnClickListener {


            productDetailViewModel.onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        showSnackBar(getString(R.string.success_addToCart))
                    }
                })
        }

        productDetailBackBtn.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}