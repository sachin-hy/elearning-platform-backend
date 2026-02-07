package com.example.project.configuration;


import java.util.Arrays;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.project.filter.JwtFilter;
import com.example.project.service.SecurityCustomDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	
	@Autowired
	private SecurityCustomDetailService securityCustomDetailService; 
	@Autowired
	private JwtFilter jwtFilter;
	
	
	String[] freePaths = {"/auth/*","/categories/*/courses","/categories","/course/courses/size","/chat/**","/app/**","/topic/**","/course/d/*"};
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
		   .cors(cors -> cors.configurationSource(corsConfigurationSource()))
		   .authorizeHttpRequests(auth -> auth
				   .requestMatchers(freePaths).permitAll()
				   .requestMatchers(HttpMethod.POST,"/categories").hasRole("ADMIN")
				   .requestMatchers("/course/create-course","/course/courses-instructor","/subsection/*","/course/delete-course","/create-section","/delete-section").hasRole("INSTRUCTOR")
				   .requestMatchers("/course/courses-student","/rating-and-review/*","/create-order","/verify-payment").hasAnyRole("STUDENT")
				   .anyRequest().authenticated()
				   ).csrf(csrf -> csrf.disable())
		   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
		   .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
	
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityCustomDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
	
	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://elearning-platform-frontend-rho.vercel.app",
                                                "http://localhost:3000",
                                                "https://elearning-platform-frontend-8o0m.onrender.com")); ///http://localhost:3000
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization")); 
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    @Bean
    public Tika checkFile()
    {
    	return new Tika();
    }
    
}
