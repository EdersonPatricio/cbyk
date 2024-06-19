package com.cbyk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
		http.authorizeHttpRequests( 
			( authz ) -> authz.requestMatchers( HttpMethod.OPTIONS, "/**" ).permitAll()
				.requestMatchers( "/swagger-ui/**" ).permitAll()
				.requestMatchers( "/v3/api-docs/**" ).permitAll()
				.anyRequest().authenticated() 
		).csrf( csrf -> csrf.disable() )
		.httpBasic( Customizer.withDefaults() );

		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService( PasswordEncoder passwordEncoder ) {
		var userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser( User.withUsername( "user" )
			.password( passwordEncoder.encode( "password" ) )
			.roles( "USER" )
			.build() );
        
        return userDetailsManager;
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
