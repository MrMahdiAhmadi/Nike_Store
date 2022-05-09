package com.example.nikestore.feature.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Product
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.NikeImageView

class FavoriteProductAdapter(
    val products: MutableList<Product>,
    val favoriteProductEventListener: FavoriteProductEventListener,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<FavoriteProductAdapter.viewHolder>() {

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTv = itemView.findViewById<TextView>(R.id.productFavTitleTv)
        private val productIv = itemView.findViewById<NikeImageView>(R.id.productFavImageView)
        fun bindProduct(product: Product) {
            titleTv.text = product.title
            imageLoadingService.load(productIv, product.image)
            itemView.setOnClickListener {
                favoriteProductEventListener.onClick(product)
            }
            itemView.setOnLongClickListener {
                products.remove(product)
                notifyItemRemoved(adapterPosition)
                favoriteProductEventListener.onLongClick(product)
                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bindProduct(products[position])
    }

    override fun getItemCount(): Int = products.size

    interface FavoriteProductEventListener {
        fun onClick(product: Product)
        fun onLongClick(product: Product)
    }
}

