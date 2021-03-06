package com.orderservice.orderservice.domain.events

import com.orderservice.orderservice.domain.entities.Order

abstract class Event {
    abstract val eventId: String

    companion object {
        fun createEvent(order: Any): Event =
            OrderEvent.create(
                order = order as Order
            )
    }
}
