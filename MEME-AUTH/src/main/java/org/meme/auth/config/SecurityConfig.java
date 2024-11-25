package org.meme.auth.config;

import lombok.RequiredArgsConstructor;
import org.meme.auth.jwt.JwtAccessDeniedHandler;
import org.meme.auth.jwt.JwtAuthenticationEntryPoint;
import org.meme.auth.jwt.JwtCustomAuthenticationFilter;
import org.meme.auth.service.PrincipalDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PrincipalDetailsService userDetailService;
    private final JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)  // 401
                                .accessDeniedHandler(accessDeniedHandler)  // 403
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))  // H2 데이터베이스 웹 콘솔 허용
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/api/v1/signup/model").permitAll()
                                .requestMatchers("/api/v1/signup/artist").permitAll()
                                .requestMatchers("/api/v1/reissue").permitAll()
                                .requestMatchers("/api/v1/check/*").permitAll()
                                .requestMatchers("/api/v1/auth/artist/extra").permitAll()
                                .requestMatchers("/api/v1/auth/logout").permitAll()
                                .requestMatchers("/api/v1/auth/withdraw").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll() // 모든 Actuator 엔드포인트 허용
                                .requestMatchers("/api/v2/**").permitAll()  // API version update
                                .requestMatchers("/h2-console/**").permitAll()  // H2 데이터베이스 경로 허용
                );


        http.addFilterBefore(jwtCustomAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
