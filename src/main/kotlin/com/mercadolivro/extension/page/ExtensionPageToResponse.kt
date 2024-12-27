package com.mercadolivro.extension.page

import com.mercadolivro.controller.response.page.PageResponse
import org.springframework.data.domain.Page

fun <T> Page<T>.toPageResponse() = PageResponse<T>(
    items = this.content,
    totalItems = this.totalElements,
    currentPage = this.number,
    totalPages = this.totalPages
)