package ua.tss.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import ua.tss.service.UserService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	  		
	  @Autowired
	    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/mainPage","/signup").permitAll() 
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
        
        
        
    }
    
    @Override
    public void configure(final WebSecurity web) throws Exception {

                web.ignoring()
                	.antMatchers("/resources/**")
                	.antMatchers("/static/**")
                	.antMatchers("/images/**")
                	.antMatchers("/css/**")
                	.antMatchers("/js/**")
                //.antMatchers("/webjars/**")
        ;   
    }
    
    
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
    
    @Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}
    
 
    
     
	   /**	
	   @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) 
	      throws Exception {
	        auth
	          .inMemoryAuthentication()
	          .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
	          .and()
	          .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
	          .and()
	          .withUser("supervior").password(passwordEncoder().encode("supervior")).roles("SUPERVISOR");
	          
	    }
	   

  **/
   


}
