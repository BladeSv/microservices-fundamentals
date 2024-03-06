package by.mitr.storageservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> {
//                    auth
//                            .requestMatchers("/actuator/**").permitAll()
//                            .anyRequest().authenticated();
//                })
//                .oauth2Login(oath2 -> {
//                    oath2.loginPage("/login").permitAll();
//                    oath2.successHandler(oAuth2LoginSuccessHandler);
//                })
//                .build();
//    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/articles/**")
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/actuator/**").permitAll()
                                .anyRequest().hasAuthority("SCOPE_storage.write")
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/actuator/**").permitAll()
//                            .anyRequest().authenticated();
//                }).oauth2Login(Customizer.withDefaults());
//
//        return http.build();
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of(frontendUrl));
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
//        return urlBasedCorsConfigurationSource;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/user/**").hasRole("USER")
//                        .anyRequest().authenticated()
//                );
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((authz) -> authz
////                                .requestMatchers(HttpMethod.GET, "/storages").permitAll()
//                                .requestMatchers("/actuator/**")
//                                .permitAll()
////                        .requestMatchers(HttpMethod.POST, "/storages").hasAuthority("admin.write")
////                        .requestMatchers(HttpMethod.DELETE, "/storages").hasAuthority("admin.write")
//                                .anyRequest().authenticated()
//                );
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers(HttpMethod.GET, "/storages")
//                        .permitAll()
//                        .requestMatchers("/actuator/**")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.POST, "/storages").hasAuthority("admin.write")
//                        .requestMatchers(HttpMethod.DELETE, "/storages").hasAuthority("admin.write")
//                        .anyRequest().authenticated()
//                );
//        return http.build();
//    }


}
