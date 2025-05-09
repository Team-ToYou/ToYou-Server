package com.example.toyou.common.config;


import com.example.toyou.common.jwt.JwtAccessDeniedHandler;
import com.example.toyou.common.jwt.JwtAuthenticationEntryPoint;
import com.example.toyou.common.jwt.TokenAuthenticationFilter;
import com.example.toyou.common.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    /**
     * 스프링 시큐리티 기능 비활성화
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                // 정적 리소스에 대한 스프링 시큐리티 사용을 비활성화
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //csrf disable
                .formLogin(AbstractHttpConfigurer::disable) //Form 로그인 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable) //HTTP Basic 인증 방식 disable

                // 토큰 기반 인증을 사용하기 때문에 세션 기능 비활성화
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증, 인가 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("/users/nickname/check", "/health", "/auth/**", "/fcm/topic", "/users/test/**", "/login/**")
                        .permitAll()
                        .anyRequest().authenticated())

                // 헤더를 확인할 커스텀 필터 추가
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // 인증 및 인가 예외 처리
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }
}
