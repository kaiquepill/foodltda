package com.foodltda.merchantservice.domain.service

import com.foodltda.merchantservice.application.dto.request.ProductDTO
import com.foodltda.merchantservice.application.dto.request.UpdateProduct
import com.foodltda.merchantservice.application.dto.response.Response
import com.foodltda.merchantservice.domain.entities.Products
import com.foodltda.merchantservice.domain.entities.Restaurant
import com.foodltda.merchantservice.domain.exceptions.TagNotFoundException
import com.foodltda.merchantservice.resouce.repositories.ProductsRepository
import com.foodltda.merchantservice.resouce.repositories.RestaurantRepository
import com.foodltda.merchantservice.resouce.repositories.TagRepository
import com.github.slugify.Slugify
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import java.util.*
import kotlin.math.log

@Service
class ProductService(val productsRepository: ProductsRepository,
                     val restaurantRepository: RestaurantRepository,
                     val restaurantService: RestaurantService,
                     val tagRepository: TagRepository) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ProductService::class.java.name)
    }

    fun register(personId: String, productDTO: ProductDTO, result: BindingResult, response: Response<Any>): Response<Any> {
        val restaurant = restaurantService.getRestaurantByPersonId(personId, response).data as Restaurant

        var slug = Slugify().slugify(productDTO.name)

        if (productsRepository.existsBySlug(slug)) {
            slug += "-" + UUID.randomUUID().toString().substring(0, 8)
        }

        val tag = productDTO.tag.let {
            tagRepository.findByName(it) ?: throw TagNotFoundException("Tag: $it not be exist")
        }

        restaurant.id?.let { Products.fromDocument(productDTO, slug, it, tag) }?.let {product ->
            productsRepository.save(product).let {
                restaurant.products.add(it.slug)
                restaurantRepository.save(restaurant)
                logger.info("Create product: ${it.id}")
                response.data = it
            }
        }
        return response
    }

    fun update(slug: String, product: UpdateProduct, response: Response<Any>): Response<Any> {
        productsRepository.findBySlug(slug)?.let {
            var slug = it.slug

            val restaurant = restaurantRepository.findById(it.restaurantId)
            restaurant.get().products.remove(slug)

            product.name?.let { new ->
                if (new != it.name) {
                    slug = Slugify().slugify(product.name)
                    if (productsRepository.existsBySlug(slug!!)) {
                        slug += "-" + UUID.randomUUID().toString().substring(0, 8)
                    }
                }
            }

            val update = it.copy(
                    id = it.id,
                    restaurantId = it.restaurantId,
                    slug = slug,
                    name = product.name ?: it.name,
                    image = product.image ?: it.image,
                    price = product.price ?: it.price,
                    description = product.description ?: it.description,
                    tag = product.tag?.let { tag ->
                        tagRepository.findByName(tag) ?: throw TagNotFoundException("Tag: $it not be exist")
                    } ?: it.tag
            )

            restaurant.get().products.add(slug)
            restaurantRepository.save(restaurant.get())
            response.data = productsRepository.save(update)
            logger.info("Update product: ${it.id}")
        }

        return response
    }
}