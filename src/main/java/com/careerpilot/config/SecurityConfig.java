package com.careerpilot.config;
import com.careerpilot.security.CustomUserDetailsService;
import com.careerpilot.security.RoleBasedLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    private final RoleBasedLoginSuccessHandler loginSuccessHandler;


    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            RoleBasedLoginSuccessHandler loginSuccessHandler) {

        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
    }


    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================
    // AUTHENTICATION PROVIDER
    // =========================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }


    // =========================
    // SECURITY FILTER CHAIN
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                .authenticationProvider(
                        authenticationProvider()
                )

                .csrf(csrf -> csrf.disable())


                // =========================
                // AUTHORIZATION
                // =========================

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC PAGES
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",

                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()


                        // PUBLIC USER APIs
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()


                        // STUDENT / AUTHENTICATED PAGES
                        .requestMatchers(
                                "/dashboard.html",
                                "/interview.html",
                                "/resume.html",
                                "/history.html",
                                "/progress.html"
                        ).authenticated()

                        .requestMatchers(
                                "/admin-dashboard.html",
                                "/admin-resumes.html",
                                "/admin/**"
                        ).hasRole("ADMIN")

                        // INTERVIEW APIs
                        .requestMatchers(
                                "/api/interview/start",
                                "/api/interview/submit"
                        ).authenticated()


                        // RESUME APIs
                        .requestMatchers(
                                "/api/resume/**"
                        ).authenticated()


                        // ADMIN PAGE
                        .requestMatchers(
                                "/admin-dashboard.html",
                                "/admin/**"
                        ).hasRole("ADMIN")


                        // ADMIN APIs
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")


                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )



                // =========================
                // SPRING SECURITY LOGIN
                // =========================

                .formLogin(form -> form

                        .loginPage("/login.html")

                        .loginProcessingUrl(
                                "/api/users/login"
                        )

                        .usernameParameter("email")

                        .passwordParameter("password")

                        .successHandler(
                                loginSuccessHandler
                        )

                        .permitAll()
                )


                // =========================
                // LOGOUT
                // =========================

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl(
                                "/index.html"
                        )

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );


        return http.build();
    }
}