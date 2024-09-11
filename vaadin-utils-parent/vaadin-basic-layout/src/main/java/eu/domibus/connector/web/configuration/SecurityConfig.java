/*
 * Copyright 2024 European Union Agency for the Operational Management of Large-Scale IT Systems
 * in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.web.configuration;

import eu.domibus.connector.web.login.LoginView;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures Spring Security.
 *
 * @author spindlest
 */
@Configuration
@EnableWebSecurity
@SuppressWarnings("squid:S1135")
public class SecurityConfig {
    // TODO: configure authentication provider!
    //  @Autowired
    //  WebUserAuthenticationProvider authProvider;

    /**
     * Creates an Authentication Provider including authProvider.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authProviders = new ArrayList<>();
        // authProviders.add(authProvider);
        return new ProviderManager(authProviders);
    }

    /**
     * ActuatorWebSecurityConfiguration is a nested static class within the SecurityConfig class
     * that extends WebSecurityConfigurerAdapter to provide specific security configurations for
     * actuator endpoints.
     */
    @Configuration
    @Order(1)
    public static class ActuatorWebSecurityConfiguration {
        private final String actuatorBasePath = "actuator";

        @Bean
        protected SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
            if (StringUtils.isNotEmpty(actuatorBasePath)) {
                http
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/" + actuatorBasePath + "/**")
                    ).httpBasic(Customizer.withDefaults())
                    .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .hasAnyRole("ACTUATOR", "ADMIN")
                    );
            }
            return http.build();
        }
    }

    /**
     * VaadinWebSecurityConfiguration is a configuration class that manages Spring Security settings
     * for a Vaadin-based web application.
     */
    @Configuration
    @Order(500)
    public static class VaadinWebSecurityConfiguration {
        private static final String LOGIN_PROCESSING_URL = "/" + LoginView.ROUTE;
        private static final String LOGIN_FAILURE_URL = "/login?error";
        private static final String LOGIN_URL = "/" + LoginView.ROUTE;
        private static final String LOGOUT_SUCCESS_URL = "/" + LoginView.ROUTE;

        @Bean
        protected SecurityFilterChain vaadinFilterChain(HttpSecurity http) throws Exception {
            // disable csrf so vaadin works!
            http
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(requestCache -> requestCache.requestCache(new CustomRequestCache()))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                    .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                    .loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .failureUrl(LOGIN_FAILURE_URL)
                )
                .logout(logout -> logout.logoutSuccessUrl(LOGOUT_SUCCESS_URL));
            return http.build();
        }

        /**
         * Allows access to static resources, bypassing Spring security.
         */
        @Bean
        public WebSecurityCustomizer vaadinWebSecurityCustomizer() {
            return web -> web.ignoring().requestMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // icons and images
                "/icons/**",
                "/images/**",

                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**",
                // (development mode) static resources
                "/frontend/**",

                // (development mode) webjars
                "/webjars/**",

                // (development mode) H2 debugging console
                "/h2-console/**",
                // allow access to webservices
                "/services/**"

            );
        }
    }

    /**
     * VaadinDevelopmentWebSecurityConfiguration is a configuration class that extends
     * WebSecurityConfigurerAdapter and customizes web security settings for a Vaadin development
     * environment.
     */
    public static class VaadinDevelopmentWebSecurityConfiguration {
        @Bean
        public WebSecurityCustomizer vaadinDevelopmentWebSecurityCustomizer() {
            return web -> web.ignoring().requestMatchers("");
        }
    }
}
