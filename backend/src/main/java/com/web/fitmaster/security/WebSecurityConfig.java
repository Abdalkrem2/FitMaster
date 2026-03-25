package com.web.fitmaster.security;

import com.web.fitmaster.model.enums.AppRole;
import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.RoleRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.security.jwt.AuthEntryPointJwt;
import com.web.fitmaster.security.jwt.AuthTokenFilter;
import com.web.fitmaster.security.services.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImp userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean // الخلاصة: عملنا الفلتر @Bean لأننا بدنا نتحكم بتسجيله وإنشائه وإدراجه في سلسلة
          // الفلاتر. الـ @Autowired بنستخدمها لما يكون الكائن مُسجّل مسبقًا كـ Bean
          // (مثلاً بـ @Component).
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    public DaoAuthenticationProvider authenticationProvider() {// AuthenticationProvider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // هو المسؤول الأساسي عن عملية المصادقة في Spring Security.
    //
    // وظيفته: يستقبل Authentication (مثلاً username + password) ويحاول يعمل
    // authenticate باستخدام الـ AuthenticationProvider المسجّل (زي
    // DaoAuthenticationProvider).

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests.requestMatchers("/api/auth/**").permitAll()
                                // .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // for h2 console
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    // i added it for connection between front and back
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));   
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // to create user data when we launch project because h2 database doesnt keep
    // data when it shut off
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {

        return args -> {

            // ===== Roles =====
            Role memberRole = roleRepository.findByRoleName(AppRole.MEMBER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.MEMBER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ADMIN)));

            Role employeeRole = roleRepository.findByRoleName(AppRole.EMPLOYEE)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.EMPLOYEE)));

            Set<Role> memberRoles = Set.of(memberRole);
            Set<Role> adminRoles = Set.of(adminRole);
            Set<Role> employeeRoles = Set.of(employeeRole);

            // ===== Create Admin =====
            if (!userRepository.existsByPhone("0780000000")) {
                User admin = new User("admin", "0780000000",
                        passwordEncoder.encode("admin"), "male", true);
                admin.setRoles(adminRoles);
                admin.setCreatedBy(admin);
                userRepository.save(admin);
            }
            // بعد إنشاء الأدمن
            User adminUser = userRepository.findByPhone("0780000000").orElseThrow();

// ===== Employees =====
            for (int i = 1; i <= 3; i++) {
                String phone = "079000000" + i;

                if (!userRepository.existsByPhone(phone)) {
                    User emp = new User("employee" + i, phone,
                            passwordEncoder.encode("emp123"), "male", true);

                    emp.setRoles(employeeRoles);
                    emp.setCreatedBy(adminUser); // 🔥 مهم

                    userRepository.save(emp);
                }
            }

// ===== Members =====
            for (int i = 1; i <= 100; i++) {
                String phone = "077000" + String.format("%04d", i);

                if (!userRepository.existsByPhone(phone)) {
                    User member = new User("member" + i, phone,
                            passwordEncoder.encode("123456"), "male", true);

                    member.setRoles(memberRoles);
                    member.setCreatedBy(adminUser); // 🔥 مهم

                    userRepository.save(member);
                }
            }


        };
    }

}
