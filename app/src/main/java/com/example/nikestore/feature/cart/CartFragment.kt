package com.example.nikestore.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.feature.auth.AuthActivity
import com.example.nikestore.feature.main.detail.ProductDetailActivity
import com.example.nikestore.feature.shipping.ShippingActivity
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.data.CartItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.view_cart_empty_state.*
import kotlinx.android.synthetic.main.view_cart_empty_state.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartFragment : NikeFragment(), CartItemAdapter.CartItemViewCallBacks {

    private val viewModel: CartViewModel by viewModel()
    var adapter: CartItemAdapter? = null
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.cartItemsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            cartRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = CartItemAdapter(it as MutableList<CartItem>, imageLoadingService, this)
            cartRv.adapter = adapter
        }

        viewModel.purchaseDetailLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            adapter?.let { cartA ->
                cartA.purchaseDetail = it
                cartA.notifyItemChanged(cartA.cartItems.size)
            }
        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            if (it.mustShow) {
                val emptyState = showEmptyState(R.layout.view_cart_empty_state)
                emptyState?.let { view ->
                    view.emptyStateMessageTv.text = getString(it.messageResId)
                    view.emptyStateCtaBtn.visibility =
                        if (it.mustShowCallToActionBtn) View.VISIBLE else View.GONE
                    view.emptyStateCtaBtn.setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            } else
                emptyStateRootView?.visibility = View.GONE
        }

        payBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, viewModel.purchaseDetailLiveData.value)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun onRemoveCartItemButtonClick(cartItem: CartItem) {
        viewModel.removeFromCart(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    adapter?.removeCartItem(cartItem)
                }

            })
    }

    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        viewModel.increaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    adapter?.increaseCount(cartItem)
                }
            })
    }

    override fun onDecreaseCartItemButtonClick(cartItem: CartItem) {
        viewModel.decreaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    adapter?.decreaseCount(cartItem)
                }
            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, cartItem.product)
        })
    }
}