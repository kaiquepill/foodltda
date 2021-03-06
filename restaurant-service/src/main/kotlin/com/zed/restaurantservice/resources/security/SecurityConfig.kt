package com.zed.restaurantservice.resources.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtUtil: JWTUtil


    @Value("\${token}")
    private lateinit var secret: String

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        print(secret)
        http.cors().and().csrf().disable()
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, *GET_MATCHER_ADM).hasRole("ADM")
            .anyRequest().authenticated()
        http.addFilter(AuthorizationFilter(authenticationManager(), jwtUtil,  secret))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val cors = CorsConfiguration().applyPermitDefaultValues()
        cors.allowedMethods = listOf("POST", "GET", "PUT", "DELETE", "OPTIONS")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", cors)
        return source
    }
//
    companion object {
//        private val POST_MATCHERS = arrayOf(
//            "/individual",
//            "/legal"
//        )
//
        private val GET_MATCHER_ADM = arrayOf(
            "/restaurant/{restaurantId}"
        )
//
//        private val GET_MATCHER_USER = arrayOf(
//            "/person"
//        )
//
    }

}
