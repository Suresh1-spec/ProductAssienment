package com.example.Fluperproductapp.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Fluperproductapp.R
import com.example.Fluperproductapp.modal.DatabaseClient
import com.example.Fluperproductapp.modal.Product
import kotlinx.android.synthetic.main.activity_product_home.*
import kotlinx.android.synthetic.main.activity_update_product.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class UpdateProductActivity : AppCompatActivity() {

    var imageInByte: ByteArray? = null
    private val OPERATION_CAPTURE_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)
        val product: Product = intent.getSerializableExtra("PRODUCT") as Product

        loadProduct(product)
        initViews(product)

    }

    private fun initViews(product: Product) {
        btnUpdateProduct.setOnClickListener {
            upDateProduct(product)
        }
        imageInByte = product.getProductPhoto()
        btnDelete.setOnClickListener {
            deleteProduct(product)
        }

        back_update.setOnClickListener {
            finish()
        }

        layoutUpdate_capture.setOnClickListener {
            val option = arrayOf("Take from Camera")
            val adapter = ArrayAdapter(
                this,
                android.R.layout.select_dialog_item, option
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            builder.setTitle("Select Option")
            builder.setAdapter(adapter,
                DialogInterface.OnClickListener { dialog, which -> // TODO Auto-generated method stub
                    Log.e("Selected Item", which.toString())
                    if (which == 0) {
                        callCamera(product)
                    }
                })
            val dialog: AlertDialog = builder.create()
            dialog.show();

        }
    }

    private fun callCamera(product: Product) {
        /* val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
        *//* if(capturedImage.exists()) {
            capturedImage.delete()
        }*//*
        //capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(this, "info.camposha.kimagepicker.fileprovider",
                capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)*/

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 0)
        intent.putExtra("aspectY", 0)
        intent.putExtra("outputX", 250)
        intent.putExtra("outputY", 200)
    }


    private fun deleteProduct(product: Product) {

        class DeleteTask() : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {


                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .productDao()
                    .delete(product);

                return null
                // ...
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...
            }

            override fun onPostExecute(avoid: Void?) {
                super.onPostExecute(avoid)
                // ...
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
                finish()
                startActivity(Intent(this@UpdateProductActivity, ProductListActivity::class.java))
            }
        }

        val st = DeleteTask()
        st.execute()

    }

    private fun upDateProduct(product: Product) {

        var sProductName = etProductUpdateName.text.toString()
        var sProductDisc = etProductUpdateDesc.text.toString()
        var sProductRegularPrice = etProductUpdateRegular.text.toString()
        var sProductSalePrice = etProductUpdateSalePrice.text.toString()
        var sProductColor = etProductUpdateColor.text.toString()
        var sProductStore = etProductUpdateStore.text.toString()

        if (sProductName.isEmpty()) {
            etProductName.setError("Name required");
            etProductName.requestFocus();
            return;
        }

        if (sProductDisc.isEmpty()) {
            etProductDesc.setError("Desc required");
            etProductDesc.requestFocus();
            return;
        }

        if (sProductRegularPrice.isEmpty()) {
            etProductRegular.setError("Regular price required");
            etProductRegular.requestFocus();
            return;
        }

        if (sProductSalePrice.isEmpty()) {
            etProductSalePrice.setError("Sale price required");
            etProductSalePrice.requestFocus();
            return;
        }

        if (sProductColor.isEmpty()) {
            etProductColor.setError("Color required");
            etProductColor.requestFocus();
            return;
        }
        if (sProductStore.isEmpty()) {
            etProductStore.setError("Store required");
            etProductStore.requestFocus();
            return;
        }



        if (ivProductUpdateImage.drawable == null) {
            Toast.makeText(this, "Please capture image", Toast.LENGTH_SHORT).show()
        }
        updateInDb(
            sProductName,
            sProductDisc,
            sProductRegularPrice,
            sProductSalePrice,
            sProductColor,
            sProductStore,
             product
        )

    }

    private fun updateInDb(
        sProductName: String,
        sProductDisc: String,
        sProductRegularPrice: String,
        sProductSalePrice: String,
        sProductColor: String,
        sProductStore: String,
        product: Product
    ) {
        class UpdateTask() : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {

                product.setName(sProductName)
                product.setDescription(sProductDisc)
                product.setRegularPrice(sProductRegularPrice)
                product.setSalePrice(sProductSalePrice)
                product.setColor(sProductColor)
                product.setStores(sProductStore)
                if (imageInByte != null) {
                    product.setProductPhoto(imageInByte!!)
                }

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .productDao()
                    .update(product);

                return null
                // ...
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...
            }

            override fun onPostExecute(avoid: Void?) {
                super.onPostExecute(avoid)
                // ...
                Toast.makeText(applicationContext, "Updated", Toast.LENGTH_LONG).show()
                finish()
                startActivity(Intent(this@UpdateProductActivity, ProductListActivity::class.java))
            }
        }

        val st = UpdateTask()
        st.execute()
    }

    private fun loadProduct(product: Product) {
        etProductUpdateName.setText(product.getName())
        etProductUpdateDesc.setText(product.getDescription())
        etProductUpdateRegular.setText(product.getRegularPrice())
        etProductUpdateSalePrice.setText(product.getSalePrice())
        etProductUpdateColor.setText(product.getColor())
        etProductUpdateStore.setText(product.getStores())
        val outImage: ByteArray? = product.getProductPhoto()
        val imageStream = ByteArrayInputStream(outImage)
        val theImage = BitmapFactory.decodeStream(imageStream)
        ivProductUpdateImage.setImageBitmap(theImage)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    //  val uri = data!!.data
                    val extras = data!!.extras
                    if (extras != null) {
                        val yourImage: Bitmap? = extras.getParcelable("data")
                        // convert bitmap to byte
                        // convert bitmap to byte
                        val stream = ByteArrayOutputStream()
                        yourImage!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        imageInByte = stream.toByteArray()
                        ivProductUpdateImage.setImageBitmap(yourImage)


                    }


                }

        }
    }
}
