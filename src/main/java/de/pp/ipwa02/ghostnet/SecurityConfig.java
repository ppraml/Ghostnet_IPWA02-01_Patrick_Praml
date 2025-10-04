package de.pp.ipwa02.ghostnet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(a -> a
                // frei
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/login", "/error").permitAll()

                // H2-Console nur ADMIN
                .requestMatchers("/h2-console/**").hasRole("ADMIN")

                // Startseite und Übersicht: nur NICHT-Gäste
                .requestMatchers(HttpMethod.GET, "/").hasAnyRole("REPORTER","SALVOR","ADMIN")
                .requestMatchers(HttpMethod.GET, "/nets").hasAnyRole("REPORTER","SALVOR","ADMIN")

                // Melden: Gast darf NUR /report (GET/POST)
                .requestMatchers(HttpMethod.GET, "/report").hasAnyRole("GUEST","REPORTER","SALVOR","ADMIN")
                .requestMatchers(HttpMethod.POST, "/report").hasAnyRole("GUEST","REPORTER","ADMIN","SALVOR")

                // Aktionen
                .requestMatchers("/nets/*/assign", "/nets/*/recover").hasAnyRole("SALVOR","ADMIN")
                .requestMatchers("/nets/*/lost").hasAnyRole("REPORTER","SALVOR","ADMIN")

                // Rest: Login noetig
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(roleBasedSuccessHandler()) // ✅ Rollenbasierte Weiterleitung
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .csrf(c -> c.ignoringRequestMatchers("/h2-console/**"))
            .headers(h -> h.frameOptions(f -> f.sameOrigin()));

        return http.build();
    }

    // nach Login: GUEST -> /report, alle anderen -> /
    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            Set<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            if (roles.contains("ROLE_GUEST")) {
                response.sendRedirect(request.getContextPath() + "/report");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        };
    }

    /** 
     * Demo-User:
    */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails reporter = User.withUsername("reporter").password(encoder.encode("reporter")).roles("REPORTER").build();
        UserDetails salvor   = User.withUsername("berger").password(encoder.encode("berger")).roles("SALVOR").build();
        UserDetails admin    = User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN").build();
        UserDetails guest    = User.withUsername("guest").password(encoder.encode("guest")).roles("GUEST").build();
        return new InMemoryUserDetailsManager(reporter, salvor, admin, guest);
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
