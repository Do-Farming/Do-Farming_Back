package com.hana.common.config;


import com.hana.api.auth.filter.CustomerAuthenticationFilter;
import com.hana.api.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;


    private static final String[] SWAGGER_URL = {
            "/api-docs",
            "/v3/api-docs/**",
            "/swagger-*/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
    };

    private final String[] GET_PERMIT_API_URL = {
            "/",
    };

    private final String[] POST_PERMIT_API_URL = {
            "/",
    };

    private final String[] PUT_PERMIT_API_URL = {
            "/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults())

                //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //FormLogin, BasicHttp 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new CustomerAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                //http.exceptionHandling((exceptionHandling) -> exceptionHandling
                //        .authenticationEntryPoint(authenticationEntryPoint)
                //        .accessDeniedHandler(accessDeniedHandler)
                //);

                // 권한 규칙 작성
                .authorizeHttpRequests(authorize -> authorize
                        // 인증 test API
                        .requestMatchers("/api/auth/list").authenticated()
                        .requestMatchers(SWAGGER_URL).permitAll()
                        .requestMatchers(GET_PERMIT_API_URL).permitAll()
                        //.requestMatchers(POST_PERMIT_API_URL).authenticated()
                        .requestMatchers(POST_PERMIT_API_URL).permitAll()
                        .requestMatchers(PUT_PERMIT_API_URL).permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of("*"));

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}