package com.example.Fluperproductapp.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product")
class Product:Serializable {
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0
    private var name: String = ""
    private var description: String = ""
    private var regularPrice: String = ""
    private var salePrice: String = ""
    private var productPhoto: ByteArray?=null
    private var color: String = ""
    private var stores: String=""

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getRegularPrice(): String {
        return regularPrice
    }

    fun setRegularPrice(regularPrice: String) {
        this.regularPrice = regularPrice
    }


    fun getSalePrice(): String {
        return salePrice
    }

    fun setSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun getProductPhoto(): ByteArray? {
        return this!!.productPhoto
    }

    fun setProductPhoto(productPhoto: ByteArray) {
        this.productPhoto = productPhoto
    }

    fun getColor(): String {
        return color
    }

    fun setColor(color: String) {
        this.color = color
    }

    fun getStores(): String {
        return stores
    }

    fun setStores(stores: String) {
        this.stores = stores
    }


}