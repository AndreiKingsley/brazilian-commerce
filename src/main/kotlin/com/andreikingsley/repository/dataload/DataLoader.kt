package com.andreikingsley.repository.dataload

import com.andreikingsley.DemoApplication
import com.andreikingsley.domain.*
import com.andreikingsley.domain.dataload.ImportJob
import com.andreikingsley.service.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.apache.commons.io.input.BOMInputStream
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class DataLoader(
    private val importJobRepository: ImportJobRepository,
    private val categoryService: CategoryService,
    private val customerService: CustomerService,
    private val sellerService: SellerService,
    private val productService: ProductService,
    private val orderService: OrderService,
    private val orderItemService: OrderItemService,
    private val paymentService: PaymentService,
    private val reviewService: ReviewService,
) : CommandLineRunner {

    @Value($$"${dataload.batch_size}")
    private var batchSize: Int = 100

    var recordsCount = 0

    open inner class TableHandler<T : Any>(
        val tableName: String,
        val csvPath: String,
        val recordToEntity: (CSVRecord) -> T,
        val saveBatch: (Iterable<T>) -> Unit,
    ) {
        private fun getCsvReader(): CSVParser {
            val csvFile =
                DemoApplication::class.java.classLoader.getResource(csvPath)?.file ?: error("$csvPath not found")
            val reader = InputStreamReader(
                BOMInputStream.builder()
                    .setInputStream(FileInputStream(csvFile))
                    .get(),
                StandardCharsets.UTF_8
            )
            return csvFormat.parse(reader)
        }

        private fun logRecords() {
            while (recordsCount >= RECORD_LOGGING_THRESHOLD) {
                recordsCount -= RECORD_LOGGING_THRESHOLD
                log.info("Loaded $RECORD_LOGGING_THRESHOLD records successfully!")
            }
        }

        private fun loadBatch(records: List<CSVRecord>) {
            saveBatch(records.map(recordToEntity))
            recordsCount += records.size
            logRecords()
        }

        /**
         * Load a table from a CSV file
         */
        fun loadTable() {
            log.info("Loading records to $tableName table from $csvPath...")
            val reader = getCsvReader()
            val buffer = mutableListOf<CSVRecord>()
            var tableRecordCount = 0
            for (record in reader) {
                tableRecordCount++
                buffer.add(record)
                if (buffer.size == batchSize) {
                    loadBatch(buffer)
                    buffer.clear()
                }
            }
            if (buffer.isNotEmpty()) {
                loadBatch(buffer)
            }
            log.info("Loaded $tableRecordCount records to $tableName table from $csvPath successfully")
        }
    }

    fun TableHandler<*>.getStatus(): ImportJob.ImportStatus =
        importJobRepository.getStatus(tableName)

    fun TableHandler<*>.markNotStarted() {
        importJobRepository.markNotStarted(tableName)
    }

    fun TableHandler<*>.markInProgress() {
        importJobRepository.markInProgress(tableName)
    }

    fun TableHandler<*>.markError(errorMessage: String) {
        importJobRepository.markError(tableName, errorMessage)
    }

    fun TableHandler<*>.markDone() {
        importJobRepository.markDone(tableName)
    }

    private fun String?.parseLocalDateTime(): LocalDateTime? {
        if (this.isNullOrEmpty() || this == "null") {
            return null
        }
        return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    private fun recordToCategory(record: CSVRecord): Category {
        return Category(
            productCategoryName = record.get("product_category_name"),
            productCategoryNameEnglish = record.get("product_category_name_english")
        )
    }

    private fun recordToCustomer(record: CSVRecord): Customer {
        return Customer(
            customerId = record.get("customer_id"),
            customerCity = record.get("customer_city"),
            customerState = record.get("customer_state"),
            customerUniqueId = record.get("customer_unique_id"),
            customerZipCodePrefix = record.get("customer_zip_code_prefix").toInt(),
        )
    }

    private fun recordToSeller(record: CSVRecord): Seller {
        return Seller(
            sellerId = record.get("seller_id"),
            sellerCity = record.get("seller_city"),
            sellerState = record.get("seller_state"),
            sellerZipCodePrefix = record.get("seller_zip_code_prefix").toInt(),
        )
    }


    private fun recordToProduct(record: CSVRecord): Product {
        return Product(
            productId = record.get("product_id"),
            productCategoryName = categoryService.getByName(record.get("product_category_name")),
            productDescriptionLenght = record.get("product_description_lenght")?.toIntOrNull(),
            productHeightCm = record.get("product_height_cm")?.toIntOrNull(),
            productLengthCm = record.get("product_length_cm")?.toIntOrNull(),
            productNameLenght = record.get("product_name_lenght")?.toIntOrNull(),
            productPhotosQty = record.get("product_photos_qty")?.toIntOrNull(),
            productWeightG = record.get("product_weight_g")?.toIntOrNull(),
            productWidthCm = record.get("product_width_cm")?.toIntOrNull(),
        )
    }

    private fun recordToOrder(record: CSVRecord): Order {
        return Order(
            orderId = record.get("order_id"),
            customer = customerService.repository.getReferenceById(record.get("customer_id")),
            orderApprovedAt = record.get("order_approved_at").parseLocalDateTime(),
            orderDeliveredCarrierDate = record.get("order_delivered_carrier_date").parseLocalDateTime(),
            orderDeliveredCustomerDate = record.get("order_delivered_customer_date").parseLocalDateTime(),
            orderEstimatedDeliveryDate = record.get("order_estimated_delivery_date").parseLocalDateTime()!!,
            orderPurchaseTimestamp = record.get("order_purchase_timestamp").parseLocalDateTime()!!,
            orderStatus = OrderStatus.valueOf(record.get("order_status").uppercase()),
        )
    }

    private fun recordToOrderItem(record: CSVRecord): OrderItem {
        val orderId = record.get("order_id")
        return OrderItem(
            orderItemId = "${orderId}_${record.get("order_item_id")}",
            order = orderService.repository.getReferenceById(orderId),
            freightValue = record.get("freight_value").toDouble(),
            price = record.get("price").toDouble(),
            product = productService.repository.getReferenceById(record.get("product_id")),
            seller = sellerService.repository.getReferenceById(record.get("seller_id")),
            shippingLimitDate = record.get("shipping_limit_date").parseLocalDateTime()!!,
        )
    }

    private fun recordToPayment(record: CSVRecord): Payment {
        val orderId = record.get("order_id")
        return Payment(
            paymentId = "${orderId}_${record.get("payment_sequential")}",
            order = orderService.repository.getReferenceById(orderId),
            paymentInstallments = record.get("payment_installments").toInt(),
            paymentSequential = record.get("payment_sequential").toInt(),
            paymentType = record.get("payment_type").toString(),
            paymentValue = record.get("payment_value").toDouble()
        )
    }

    private fun recordToReview(record: CSVRecord): Review {
        val orderId = record.get("order_id")
        return Review(
            reviewId = "${record.get("review_id")}_${orderId}",
            order = orderService.repository.getReferenceById(orderId),
            reviewAnswerTimestamp = record.get("review_answer_timestamp").parseLocalDateTime()!!,
            reviewCommentMessage = record.get("review_comment_message"),
            reviewCommentTitle = record.get("review_comment_title"),
            reviewCreationDate = record.get("review_creation_date").parseLocalDateTime()!!,
            reviewScore = record.get("review_score").toInt(),
        )
    }

    override fun run(vararg args: String) {

        val categoryHandler = TableHandler(
            "categories",
            "data/product_category_name_translation.csv",
            ::recordToCategory,
            categoryService::load,
        )

        val customerHandler = TableHandler(
            "customers",
            "data/olist_customers_dataset.csv",
            ::recordToCustomer,
            customerService::load,
        )

        val sellerHandler = TableHandler(
            "sellers",
            "data/olist_sellers_dataset.csv",
            ::recordToSeller,
            sellerService::load,
        )

        val productHandler = TableHandler(
            "products",
            "data/olist_products_dataset.csv",
            ::recordToProduct,
            productService::load,
        )

        val orderHandler = TableHandler(
            "orders",
            "data/olist_orders_dataset.csv",
            ::recordToOrder,
            orderService::load,
        )

        val orderItemHandler = TableHandler(
            "order_items",
            "data/olist_order_items_dataset.csv",
            ::recordToOrderItem,
            orderItemService::load,
        )

        val paymentHandler = TableHandler(
            "payments",
            "data/olist_order_payments_dataset.csv",
            ::recordToPayment,
            paymentService::load,
        )

        val reviewHandler = TableHandler(
            "reviews",
            "data/olist_order_reviews_dataset.csv",
            ::recordToReview,
            reviewService::load,
        )

        val handlers = listOf(
            categoryHandler,
            customerHandler,
            sellerHandler,
            productHandler,
            orderHandler,
            orderItemHandler,
            paymentHandler,
            reviewHandler
        )

        handlers.forEach { handler ->
            if (handler.getStatus() != ImportJob.ImportStatus.DONE) {
                handler.markNotStarted()
            }
        }

        handlers.forEach { handler ->
            val status = handler.getStatus()
            if (status == ImportJob.ImportStatus.DONE) {
                log.info("Data for ${handler.tableName} table was already loaded, skipping.")
            } else {
                log.info("Data for ${handler.tableName} table is not loaded, starting load")
                handler.markInProgress()
                handler.loadTable()
                handler.markDone()
                log.info("Data for ${handler.tableName} table was successfully loaded.")
            }
        }

        log.info("Data loading completed!")

    }

    companion object {
        private val csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setDelimiter(',')
            .get()

        private val log = LoggerFactory.getLogger(this::class.java)

        private const val RECORD_LOGGING_THRESHOLD = 10000
    }
}