package com.andreikingsley.controller

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.OrderStatus
import com.andreikingsley.domain.dto.OrderFilter
import com.andreikingsley.domain.dto.toOrderDto
import com.andreikingsley.domain.dto.toReviewDto
import com.andreikingsley.domain.dto.toSpecification
import com.andreikingsley.service.OrderItemService
import com.andreikingsley.service.OrderService
import com.andreikingsley.service.PaymentService
import com.andreikingsley.service.ReviewService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
class OrdersController(
    private val orderService: OrderService,
    private val reviewService: ReviewService,
    private val orderItemService: OrderItemService,
    private val paymentService: PaymentService,
) {

    @GetMapping("/orders")
    fun orders(
        model: Model,
        @ModelAttribute("orderFilter") orderFilter: OrderFilter,
        @PageableDefault(size = 20, sort = ["orderPurchaseTimestamp"], direction = Sort.Direction.DESC)
        pageable: Pageable,
        @RequestParam(defaultValue = "false") fragment: Boolean,
    ): String {


        val ordersPage = orderService.getPage(orderFilter, pageable)

        model.addAttribute("orders", ordersPage)
        model.addAttribute("statuses", OrderStatus.entries)

        return if (fragment) "orders_view :: content" else "orders_view"
    }

    @GetMapping("/orders/{id}")
    fun orderCard(model: Model, @PathVariable id: String): String {

        val orderCard = orderService.getCard(id) ?: throw OrderNotFoundException(id)

        model.addAttribute(
            "orderCard",
            orderCard
        )

        return "order_card_view"
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class OrderNotFoundException(id: String) : RuntimeException("Order $id not found")
}
