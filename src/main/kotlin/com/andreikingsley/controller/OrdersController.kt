package com.andreikingsley.controller

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.OrderStatus
import com.andreikingsley.domain.dto.OrderFilter
import com.andreikingsley.domain.dto.toOrderDto
import com.andreikingsley.domain.dto.toReviewDto
import com.andreikingsley.domain.dto.toSpecification
import com.andreikingsley.service.OrderService
import com.andreikingsley.service.ReviewService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@Controller
class OrdersController(val orderService: OrderService, val reviewService: ReviewService) {

    private val rowsPerPage = 20

    @GetMapping("/orders")
    fun orders(
        model: Model,
        @RequestParam(defaultValue = "0") page: Int,
        @ModelAttribute("orderFilter") orderFilter: OrderFilter,
        @RequestParam(defaultValue = "false") fragment: Boolean,
    ): String {

        val ordersPage = orderService.repository.findAll(
            orderFilter.toSpecification(),
            PageRequest.of(page, rowsPerPage, Sort.by(Order::orderPurchaseTimestamp)),
        )

        model.addAttribute("orders", ordersPage.map { it.toOrderDto() }.content)
        model.addAttribute("page", page)
        model.addAttribute("hasPrev", ordersPage.hasPrevious())
        model.addAttribute("hasNext", ordersPage.hasNext())
        model.addAttribute("statuses", OrderStatus.entries)

        return if (fragment) "orders_view :: content" else "orders_view"
    }

    @GetMapping("/orders/{id}")
    fun orderCard(model: Model, @PathVariable id: String): String {

        val reviews = reviewService.repository.getAllByOrderOrderId(id).map { it.toReviewDto() }

        model.addAttribute(
            "order",
            orderService.repository.findById(id).map { it.toOrderDto() }.orElse(null) ?: error("Order not found")
        )
        model.addAttribute("reviews", reviews)

        return "order_card_view"
    }
}
