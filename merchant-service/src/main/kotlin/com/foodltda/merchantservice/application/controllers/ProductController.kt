package com.foodltda.merchantservice.application.controllers

import com.foodltda.merchantservice.application.dto.request.ProductDTO
import com.foodltda.merchantservice.application.dto.response.Response
import com.foodltda.merchantservice.domain.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class ProductController(val productService: ProductService) {

    @PostMapping("/register/{personId}")
    fun register(@PathVariable personId: String, @Valid @RequestBody productDTO: ProductDTO, result: BindingResult): ResponseEntity<Response<Any>> {
        val response =  Response<Any>()
        productService.register(personId, productDTO, result, response)

        return ResponseEntity.ok(response)
    }
    @GetMapping
    fun products(){}
    @DeleteMapping
    fun delete(){}
    @PutMapping
    fun update(){}
}