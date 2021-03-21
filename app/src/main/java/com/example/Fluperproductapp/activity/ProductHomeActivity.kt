package com.example.Fluperproductapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.Fluperproductapp.R
import com.example.Fluperproductapp.modal.DatabaseClient
import com.example.Fluperproductapp.modal.Product
import kotlinx.android.synthetic.main.activity_product_home.*
import java.io.*


class ProductHomeActivity : AppCompatActivity() {
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val REQUEST_IMAGE_CAPTURE = 1
    private var mUri: Uri? = null
    private val OPERATION_CAPTURE_PHOTO = 1
    private val PERMISSION_REQUEST_CODE = 200
    var imagePath: String? = null
    var imageInByte: ByteArray? = null
    var file: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_home)

        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(
            this, PERMISSIONS_STORAGE,
            REQUEST_EXTERNAL_STORAGE
        )
        initViews()
    }

    private fun initViews() {

        btnCreateProject.setOnClickListener {
            createProduct()
        }

        btnShowList.setOnClickListener {
            var intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }

        if (checkPermission()) {
            layout_capture.setOnClickListener {
                selectImage()
            }
        } else {
            requestPermission();
        }

    }
    private fun selectImage() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            false
        } else true
    }

    private fun callCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 0)
        intent.putExtra("aspectY", 0)
        intent.putExtra("outputX", 250)
        intent.putExtra("outputY", 200)
    }

    private fun createProduct() {
        var sProductName = etProductName.text.toString()
        var sProductDisc = etProductDesc.text.toString()
        var sProductRegularPrice = etProductRegular.text.toString()
        var sProductSalePrice = etProductSalePrice.text.toString()
        var sProductColor = etProductColor.text.toString()
        var sProductStore = etProductStore.text.toString()

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

        if (ivProductImage.drawable == null) {
            Toast.makeText(this, "Please capture image", Toast.LENGTH_SHORT).show()
            return;
        }

        saveInDb(
            sProductName,
            sProductDisc,
            sProductRegularPrice,
            sProductSalePrice,
            sProductColor,
            sProductStore,
            imagePath
        )
    }

    private fun saveInDb(
        sProductName: String,
        sProductDisc: String,
        sProductRegularPrice: String,
        sProductSalePrice: String,
        sProductColor: String,
        sProductStore: String,
        imagePath: String?
    ) {
        class SaveTask() : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {

                var product = Product()
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
                    .insert(product);

                return null
                // ...
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                // ...
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                etProductName.setText("")
                etProductDesc.setText("")
                etProductRegular.setText("")
                etProductSalePrice.setText("")
                etProductColor.setText("")
                etProductStore.setText("")
                ivProductImage.setImageResource(android.R.color.transparent)

            }
        }

        val st = SaveTask()
        st.execute()
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                onCaptureImageResult(data!!, ivProductImage)

            }
        }

    }
    private fun onCaptureImageResult(
        data: Intent,
        view: ImageView

    ) {
        val thumbnail = data.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        imageInByte = bytes.toByteArray()
        view.setImageBitmap(thumbnail)
        view.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    applicationContext,
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()

                // main logic
                layout_capture.setOnClickListener {
                    selectImage()
                }

            } else {
                Toast.makeText(
                    applicationContext,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel("You need to allow access permissions",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermission()
                                }
                            })
                    }
                }
            }


        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}
