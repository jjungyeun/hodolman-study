package com.wonjung.hodolstudy1.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

//    @Bean
//    fun webSecurityCustomizer(): WebSecurityCustomizer {
//        // 특정 경로들에 대해 security 비활성화
//        return WebSecurityCustomizer {
//            it.ignoring().requestMatchers("/favicon.ico", "/error")
//                .requestMatchers(AntPathRequestMatcher("/h2-console/**"))
//        }
//    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .csrf {
                it.disable()    // CSRF 설정 비활성화
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/favicon.ico", "/error", "h2-console/**").permitAll()
                    .requestMatchers("/auth/login", "/login-page").permitAll()    // 로그인 관련 경로는 모두 접근 가능하도록 설정
                    .anyRequest().authenticated()                                   // 그 외 경로는 인증 받아야 함
            }
            .formLogin {
                it.loginPage("/login-page")             // 로그인 폼 페이지
                    .loginProcessingUrl("/auth/login")  // 로그인을 처리하는 서버 url
                    .usernameParameter("username")      // username 관련 파라미터 이름
                    .passwordParameter("password")      // password 관련 파라미터 이름
                    .defaultSuccessUrl("/")             // 로그인 성공 시 이동할 url
            }
            .userDetailsService(userDetailsService())
            .rememberMe {   // 자동로그인 관련 설정
                it.rememberMeParameter("remember")
                    .alwaysRemember(false)
                    .tokenValiditySeconds(3600*24*30)                 // 얼마동안 유효하게 할건지
            }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val userDetailsManager = InMemoryUserDetailsManager()
        val user = User.withUsername("hello")
            .password(passwordEncoder().encode("1234"))
            .roles("ADMIN")
            .build()
        userDetailsManager.createUser(user)
        return userDetailsManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return SCryptPasswordEncoder(16, 8, 1, 32, 64)
    }
}