package com.example.nikestore.feature.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.view.NikeImageView

class OrderHistoryItemAdapter(val context: Context, val orderHistoryItems: List<OrderHistoryItem>) :
    RecyclerView.Adapter<OrderHistoryItemAdapter.viewHolder>() {

    val layoutParams: LinearLayout.LayoutParams

    init {
        val size = convertDpToPixel(100f, context).toInt()
        val margin = convertDpToPixel(8f, context).toInt()
        layoutParams = LinearLayout.LayoutParams(size, size)
        layoutParams.setMargins(margin, 0, margin, 0)
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val orderId = itemView.findViewById<TextView>(R.id.orderIdTv)
        private val price = itemView.findViewById<TextView>(R.id.orderPriceTv)
        private val orderProductLl = itemView.findViewById<LinearLayout>(R.id.orderProductLl)

        fun bind(orderHistoryItem: OrderHistoryItem) {

            orderId.text = orderHistoryItem.id.toString()
            price.text = orderHistoryItem.payable.toString()
            orderProductLl.removeAllViews()

            orderHistoryItem.order_items.forEach {
                val imageView = NikeImageView(context)
                imageView.layoutParams = layoutParams
                imageView.setImageURI(it.product.image)
                orderProductLl.addView(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder =
        viewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false))

    override fun onBindViewHolder(holder: viewHolder, position: Int) =
        holder.bind(orderHistoryItems[position])

    override fun getItemCount(): Int = orderHistoryItems.size
}