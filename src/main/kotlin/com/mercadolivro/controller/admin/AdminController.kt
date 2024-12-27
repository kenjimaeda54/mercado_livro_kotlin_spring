package com.mercadolivro.controller.admin


import org.springframework.web.bind.annotation.*

private const val REQUEST_MAPPING = "admin"

@RestController
@RequestMapping(REQUEST_MAPPING)
class AdminController{

    @GetMapping("/report")
    fun report(): String = "This is report admin"
}