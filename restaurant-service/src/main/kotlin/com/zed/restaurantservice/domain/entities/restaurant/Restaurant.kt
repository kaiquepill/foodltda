package com.zed.restaurantservice.domain.entities.restaurant

import com.fasterxml.jackson.annotation.JsonIgnore
import com.zed.restaurantservice.domain.entities.filter.Payment
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Restaurant(
    @Id
    val _id: String? = null,
    val personId: String? = null,
    val slug: String? = null,
    val name: String,
    val image: String,
    val address: Address,
    val phone: Phone,
    @JsonIgnore
    val openingHours: MutableList<DeliveryTime> = mutableListOf(),
    val category: String,
    val payments: MutableList<Payment>,
    val products: MutableList<Product> = mutableListOf(),
    val productsCategories: MutableList<ProductCategory> = mutableListOf(),
)
