package com.example.nikestore.feature.order

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.data.repo.order.OrderRepository

class OrderHistoryViewModel(orderRepository: OrderRepository) : NikeViewModel() {

    val orders = MutableLiveData<List<OrderHistoryItem>>()

    init {
        progressBarLiveData.value = true
        orderRepository.list().asyncNetworkRequest().doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<OrderHistoryItem>>(compositeDisposable) {
                override fun onSuccess(t: List<OrderHistoryItem>) {
                    orders.value = t
                }
            })
    }
}