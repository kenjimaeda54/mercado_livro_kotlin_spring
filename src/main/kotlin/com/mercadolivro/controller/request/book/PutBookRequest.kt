package com.mercadolivro.controller.request.book

import java.math.BigDecimal

//ideal do put e sempre opcionaal ja que seria atualiiar pequenas partes
data class PutBookRequest (
    val name: String?,
    val price: BigDecimal?
)