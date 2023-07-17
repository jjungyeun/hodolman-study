package com.wonjung.hodolstudy1.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.error.Http401Handler
import com.wonjung.hodolstudy1.error.Http403Handler
import com.wonjung.hodolstudy1.error.LoginFailHandler
import com.wonjung.hodolstudy1.repository.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Service


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
    fun securityFilterChain(
        http: HttpSecurity,
        objectMapper: ObjectMapper
    ): SecurityFilterChain? {
        http
            .csrf {
                it.disable()    // CSRF 설정 비활성화
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/favicon.ico", "/error", "h2-console/**").permitAll()
                    .requestMatchers("/auth/signup", "/auth/login", "/login-page").permitAll()    // 로그인 관련 경로는 모두 접근 가능하도록 설정
                    .requestMatchers("/member").hasAnyRole("ADMIN", "MEMBER")   // MEMBER 권한이 있어야 접근 가능
                    .requestMatchers("/admin").hasRole("ADMIN")   // ADMIN 권한이 있어야 접근 가능
                    .anyRequest().authenticated()                                   // 그 외 경로는 인증 받아야 함
            }
            .formLogin {
                it.loginPage("/login-page")             // 로그인 폼 페이지
                    .loginProcessingUrl("/auth/login")  // 로그인을 처리하는 서버 url
                    .usernameParameter("username")      // username 관련 파라미터 이름
                    .passwordParameter("password")      // password 관련 파라미터 이름
                    .defaultSuccessUrl("/")             // 로그인 성공 시 이동할 url
                    .failureHandler(LoginFailHandler(objectMapper))
            }
//            .userDetailsService(userDetailsService()) // 빈으로 등록하면 자동으로 등록됨
            .rememberMe {   // 자동로그인 관련 설정
                it.rememberMeParameter("remember")
                    .alwaysRemember(false)
                    .tokenValiditySeconds(3600*24*30)                 // 얼마동안 유효하게 할건지
            }

            .exceptionHandling {
                it.accessDeniedHandler(Http403Handler(objectMapper))
                    .authenticationEntryPoint(Http401Handler(objectMapper))
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return SCryptPasswordEncoder(16, 8, 1, 32, 64)
    }
}

@Service
class CustomUserDetailsService(
    val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByEmail(username)
//            .orElseThrow { MemberNotFoundException() }
            .orElseThrow { UsernameNotFoundException("${username}을 찾을 수 없습니다.") }
        return CustomUserDetails(member)
    }

}

class CustomUserDetails(
    val userId: Long,
    username: String, password: String, authorities: Collection<GrantedAuthority>
): User(username, password, authorities) {

    constructor(member: Member) : this(
        member.id,
        member.email,
        member.password,
        listOf(SimpleGrantedAuthority("ROLE_MEMBER") // Authority 이름에 "ROLE_" prefix를 붙이면 역할로 인식
        ))
}