package io.jokers.e_maryam.configuration;

import io.jokers.e_maryam.Handler.CustomAccessDeniedHandler;
import io.jokers.e_maryam.Handler.CustomAuthenticationEntryPoint;
import io.jokers.e_maryam.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String LOCALHOST4200 = "LOCALHOST:4200";
    public static final String LOCALHOST3000 = "LOCALHOST:3000";

    private static final String[] PUBLIC_URLS = { "/user/login/**" , "/user/register/**", "/user/verify/code/**", "/user/resetpassword/**", "/user/verify/password/**"};

    private final BCryptPasswordEncoder encoder;
    private final UserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                //.cors(configure->configure.configurationSource(corsConfiguration()))
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception->exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(request->request
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(OPTIONS).permitAll()
                        .requestMatchers(DELETE,"/user/delete/**").hasAnyAuthority("DELETE:USER")
                        .requestMatchers(DELETE, "/customer/delete/**").hasAnyAuthority("CUSTOMER:DELETE")
                        .anyRequest().authenticated())
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
}












/*    @Bean
    public CorsConfigurationSource corsConfiguration() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of(LOCALHOST4200, LOCALHOST3000));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "X-Requested-With"));
        return (CorsConfigurationSource) corsConfiguration;
    }*/