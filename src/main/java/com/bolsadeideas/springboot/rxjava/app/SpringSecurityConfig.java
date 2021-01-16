package com.bolsadeideas.springboot.rxjava.app;

//import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.rxjava.app.auth.filter.JWTAuthenticationFilter;
import com.bolsadeideas.springboot.rxjava.app.auth.filter.JWTAuthorizationFilter;
import com.bolsadeideas.springboot.rxjava.app.models.service.IUserDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//@Autowired
	//private DataSource dataSource;
	
	@Autowired
	private IUserDetailsService iUserDetailsService;
	
	
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
		httpSecurity 
		.authorizeRequests().antMatchers("/", "/css/**", "*/js/**", "/images/**", "/h2-console/**").permitAll()
		.anyRequest().authenticated()
        .and().headers().frameOptions().sameOrigin()
        .and().csrf().disable()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .addFilter(new JWTAuthorizationFilter(authenticationManager()))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
	
	/*
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = passwordEncoder;
		UserBuilder users = User.builder().passwordEncoder(encoder::encode); //(password->encoder.encode(password));
		
		auth.inMemoryAuthentication()
			.withUser(users.username("admin").password("12345").roles("ADMIN", "USERS"))
			.withUser(users.username("christopher").password("12345").roles("USERS"))
		;
	}
	*/
	
	/*
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception
	{
		build.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where username=?");
	}
	*/
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception
	{
		build.userDetailsService(iUserDetailsService)
		.passwordEncoder(passwordEncoder);
	}

}
