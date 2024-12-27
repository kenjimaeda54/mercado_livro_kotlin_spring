package com.mercadolivro.config

import com.mercadolivro.enums.ProfileRoles
import com.mercadolivro.respository.customer.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.AuthorizationEntryPoint
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.customer.UserCustomDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
class Security(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val customerRepository: CustomerRepository,
    private val userCustomDetailsService: UserCustomDetailsService,
    private val jwtUtil: JwtUtil,
    private  val authorizationEntryPoint: AuthorizationEntryPoint,
) {

    private val lisMatchers = arrayOf(
        "/customers"
    )

    //tudo que estiver em /admin sera um matcher
    //admin/repor exemplo
    private val ADMIN_MATCHERS = arrayOf(
        "/admin/**"
    )

    private val SWAGGER_MATCHERS = arrayOf(
        "/v2/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/v3/api-docs/**",
        "swagger-ui/**",
    )

    @Bean
    fun bcryptEncoder() = BCryptPasswordEncoder()

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(bcryptEncoder())
        authenticationProvider.setUserDetailsService(userCustomDetailsService)
        return authenticationProvider
    }

    @Bean
    fun configureFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { csrf ->
            csrf.disable()
        }.cors { cors ->
            cors.disable()
        }.sessionManagement { session ->
            //nunca vai ter um authenticate vivo na aplicaçao toda
            //precisa sempre autenticar para ter acesso
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.authorizeHttpRequests { authorize ->
            //se nã passar o permitAll preciso criar regras
            //dai o admin poderia ter acesso a outras coisas
            authorize.requestMatchers(HttpMethod.POST, *lisMatchers).permitAll()

            //swwagger matche
            authorize.requestMatchers(*SWAGGER_MATCHERS).permitAll()

            //so quem possui a role de amin que vai poder ver a rota
            //anyReqquest precisa sempre ser o ultimo
            authorize.requestMatchers(*ADMIN_MATCHERS).hasAuthority(ProfileRoles.ADMIN.description)


            //qualifier outra rota precisa ser autenticado
            //sem isso aqui não ira adiantar criar o filter, pois ira continuar a dar falha
            authorize.anyRequest().authenticated()

        }.exceptionHandling{ error ->
            //lidar com erros qquando tenta usar token invalido
            error.authenticationEntryPoint(authorizationEntryPoint)
        }.addFilter(
            customAuthenticationFilter(
                authenticationManager = authenticationManager(authenticationConfiguration)
            )
        ).addFilter(
            customAuthorizationFilter(
                authenticationManager = authenticationManager(
                    authenticationConfiguration
                )
            )
        )
            .build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    //liberar cors
    @Bean
    fun corsConfig(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**",config)
        return CorsFilter(source)
    }

    @Bean
    fun customAuthenticationFilter(authenticationManager: AuthenticationManager) =
        AuthenticationFilter(authenticationManager, customerRepository, jwtUtil)

    @Bean
    fun customAuthorizationFilter(authenticationManager: AuthenticationManager) =
        AuthorizationFilter(authenticationManager, jwtUtil, userCustomDetailsService)

}