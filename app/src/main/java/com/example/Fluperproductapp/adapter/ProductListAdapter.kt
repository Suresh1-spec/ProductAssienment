package com.example.Fluperproductapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Fluperproductapp.R
import com.example.Fluperproductapp.activity.UpdateProductActivity
import com.example.Fluperproductapp.modal.Product
import java.io.ByteArrayInputStream


class ProductListAdapter(val productList: List<Product>, var context: Context) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.product_list_row, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        productList.get(position).let { holder.bindItems(it) }
        holder.btnUpdate.setOnClickListener {
            var product = productList.get(position)
            var intent = Intent(context, UpdateProductActivity::class.java)

            intent.putExtra("PRODUCT", product)

            context.startActivity(intent)
            (context as Activity).finish()


        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return productList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var btnUpdate: Button
        fun bindItems(
            productList: Product

        ) {
            val tvProductName = itemView.findViewById(R.id.tvProductName) as TextView
            val tvDescription = itemView.findViewById(R.id.tvDescription) as TextView
            val tvProductRegularPrice =
                itemView.findViewById(R.id.tvProductRegularPrice) as TextView
            val tvProductSalePrice = itemView.findViewById(R.id.tvProductSalePrice) as TextView
            val tvProductColor = itemView.findViewById(R.id.tvProductColor) as TextView
            val tvProductStore = itemView.findViewById(R.id.tvProductStore) as TextView
            val iv_productImage = itemView.findViewById(R.id.iv_productImage) as ImageView
            btnUpdate = itemView.findViewById(R.id.btnUpdate)

            tvProductName.text = productList.getName()
            tvDescription.text = productList.getDescription()
            tvProductRegularPrice.text = productList.getRegularPrice()
            tvProductSalePrice.text = productList.getSalePrice()
            tvProductColor.text = productList.getColor()
            tvProductStore.text = productList.getStores()
            val outImage: ByteArray? = productList.getProductPhoto()
            val imageStream = ByteArrayInputStream(outImage)
            val theImage = BitmapFactory.decodeStream(imageStream)
            iv_productImage.setImageBitmap(theImage)


        }
    }
}

