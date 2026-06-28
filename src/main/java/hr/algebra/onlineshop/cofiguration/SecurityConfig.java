package hr.algebra.onlineshop.cofiguration;

import hr.algebra.onlineshop.filter.JwtAuthFilter;
import hr.algebra.onlineshop.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final MyUserDetailsService userDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain mvcFilterSecurity(HttpSecurity http) {
        return http
                .securityMatcher("/mvc/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/mvc/auth/**", "/mvc/home/**", "/mvc/cart/**").permitAll()
                        .requestMatchers("/mvc/admin/**").hasRole("ADMIN")
                        .requestMatchers("/mvc/orders/**", "/mvc/cart/checkout").authenticated()
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/mvc/auth/login")
                        .loginProcessingUrl("/mvc/auth/login")
                        .defaultSuccessUrl("/mvc/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/mvc/auth/logout")
                        .logoutSuccessUrl("/mvc/home")
                )
                .build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurity(HttpSecurity http)  {
        return http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable()) //sonarQube set up as issue -> couldn't find solution so comment
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/refresh"
                        ).permitAll()
                        .anyRequest().authenticated() //this doesn't work if csrf disabled
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
