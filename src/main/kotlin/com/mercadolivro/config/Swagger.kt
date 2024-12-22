package com.mercadolivro.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.apache.catalina.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger {

    @Bean
    fun defineOpenApi(): OpenAPI = OpenAPI().info(Info().title("Mercado Livro").description("Api Mercado Livro"))

}