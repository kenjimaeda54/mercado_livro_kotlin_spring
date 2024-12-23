package com.mercadolivro.events.listeners.books

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.respository.book.BookRepository
import com.mercadolivro.service.book.BookService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BooksListener(
    private val bookService: BookService
) {

    @EventListener
    @Async
    fun purchaseBookEvent(purchaseEvent: PurchaseEvent) {
        val books = purchaseEvent.purchaseModel.books
        books.forEach {
            it.status = BooksStatus.VENDIDO
        }
        bookService.booksPurchase(books)
    }
}