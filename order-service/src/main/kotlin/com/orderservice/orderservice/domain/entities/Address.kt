package com.orderservice.orderservice.domain.entities

data class Address(
        val street: String?,
        val number: String?,
        val complement: String?,
        val district: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val postalCode: String?
)