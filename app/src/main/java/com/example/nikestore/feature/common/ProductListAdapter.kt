package com.example.nikestore.feature.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.common.implementSpringAnimationTrait
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.NikeImageView
import com.example.nikestore.data.Product
import java.lang.IllegalStateException

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2

class ProductListAdapter(
    var viewType: Int = VIEW_TYPE_ROUND,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    var productOnClickListener: ProductOnClickListener? = null
    var products = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productIv = itemView.findViewById<NikeImageView>(R.id.productIv)
        private val titleTv = itemView.findViewById<TextView>(R.id.productTitleTv)
        private val currentPriceTv = itemView.findViewById<TextView>(R.id.currentPriceTv)
        private val previousPriceTv = itemView.findViewById<TextView>(R.id.previousPriceTv)
        private val favBtn = itemView.findViewById<ImageView>(R.id.favoriteBtn)

        fun bindProduct(product: Product) {
            imageLoadingService.load(productIv, product.image)
            titleTv.text = product.title
            currentPriceTv.text = formatPrice(product.price)
            previousPriceTv.text = formatPrice(product.previous_price)
            previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                productOnClickListener?.onProductClick(product)
            }
            if (product.isFav)
                favBtn.setImageResource(R.drawable.ic_outline_favorite_fill)
            else
                favBtn.setImageResource(R.drawable.ic_favorites)

            favBtn.setOnClickListener {
                productOnClickListener?.onFavBtnClick(product)
                product.isFav = !product.isFav
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutResId = when (viewType) {
            VIEW_TYPE_ROUND -> R.layout.item_product
            VIEW_TYPE_SMALL -> R.layout.item_product_small
            VIEW_TYPE_LARGE -> R.layout.item_product_large
            else -> throw IllegalStateException("viewType is not valid")
        }

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindProduct(products[position])

    override fun getItemCount(): Int = products.size

    interface ProductOnClickListener {
        fun onProductClick(product: Product)
        fun onFavBtnClick(product: Product)
    }
}