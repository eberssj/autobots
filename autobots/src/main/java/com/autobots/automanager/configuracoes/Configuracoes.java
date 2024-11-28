package com.autobots.automanager.configuracoes;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.jwt.JwtTokenAuth;
import com.autobots.automanager.jwt.JwtTokenFilter;
import com.autobots.automanager.jwt.JwtTokenService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Configuracoes extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsServiceImpl servico;
	
	@Autowired
	private JwtTokenService jwtTokenGerador;
	
	private static final String[] rotasPublicas = { "/login", "/usuarios/cadastro", "/**", "/usuarios" };
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.cors().and().csrf().disable();
		
		http.authorizeHttpRequests().antMatchers(rotasPublicas).permitAll().anyRequest().authenticated();
		
		http.addFilter(new JwtTokenAuth(authenticationManager(), jwtTokenGerador));
		http.addFilter(new JwtTokenFilter(authenticationManager(), jwtTokenGerador, servico));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder autenticador) throws Exception{
		autenticador.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}