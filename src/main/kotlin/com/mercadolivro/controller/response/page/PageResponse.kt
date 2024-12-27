package com.mercadolivro.controller.response.page

import com.fasterxml.jackson.annotation.JsonProperty

data class PageResponse<T>(
    val items: List<T>,
    @JsonProperty("total_items")
    val totalItems: Long,
    @JsonProperty("current_page")
    val currentPage: Int,
    @JsonProperty("total_pages")
    val totalPages: Int
)
