package com.example.Fluperproductapp.activity

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Fluperproductapp.R
import com.example.Fluperproductapp.adapter.ProductListAdapter
import com.example.Fluperproductapp.modal.DatabaseClient
import com.example.Fluperproductapp.modal.Product
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_update_product.*

class ProductListActivity : AppCompatActivity() {
    private var adapter: ProductListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        back.setOnClickListener {
            finish()
        }
        initRecyclerView()
        getAllProductList()
    }

    private fun getAllProductList() {
        class GetAllProductListTask() : AsyncTask<Void, Void, List<Product>>() {
            override fun doInBackground(vararg params: Void?): List<Product>? {
                val taskList: List<Product> = DatabaseClient
                    .getInstance(applicationContext)
                    .appDatabase
                    .productDao()
                    .getAll() as List<Product>

                return taskList
                // ...
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...
            }

            override fun onPostExecute(productList:List<Product>) {
                super.onPostExecute(productList)
                // ...
                initRecyclerView()
                if (productList!=null && productList.size>0) {
                    
                    adapter = ProductListAdapter(productList,this@ProductListActivity)
                    rv_productList.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                } else {
                    Toast.makeText(getApplicationContext(), "Product is not available", Toast.LENGTH_LONG).show();

                }

            }
        }

        val st = GetAllProductListTask()
        st.execute()
    }

    private fun initRecyclerView() {
        rv_productList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_productList.layoutManager = layoutManager
    }
}
