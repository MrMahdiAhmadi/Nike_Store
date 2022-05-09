package com.example.nikestore.data.repo.order

import com.example.nikestore.data.Checkout
import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.data.SubmitOrderResult
import io.reactivex.Single

class OrderRepositoryImpl(val orderDataSource: OrderDataSource) : OrderRepository {

    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> =
        orderDataSource.submit(firstName, lastName, postalCode, phoneNumber, address, paymentMethod)

    override fun checkout(orderId: Int): Single<Checkout> = orderDataSource.checkout(orderId)

    override fun list(): Single<List<OrderHistoryItem>> = orderDataSource.list()

}