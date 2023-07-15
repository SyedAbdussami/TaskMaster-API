package com.taskManager.Tasks.Security;

import com.taskManager.Tasks.Enum.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf()
//                .ignoringRequestMatchers(toH2Console())
//                .disable()
//                .authorizeHttpRequests()
////                .requestMatchers("/**").permitAll()
//                .requestMatchers(toH2Console()).permitAll()
//                .anyRequest().permitAll()
//                .and()
//                .httpBasic();
//        httpSecurity.headers()
//                .frameOptions()
//                .disable();


        httpSecurity.csrf().disable();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/project/**").hasAnyRole("ROLE_ADMIN","ROLE_MANAGER")
//                .requestMatchers("/api/project/**").authenticated()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.headers().frameOptions().disable();

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    UserDetailsService users() {
//        UserDetails admin = User.builder().username("user1").password("pass1").roles("ADMIN").build();
//
//        UserDetails manager = User.builder().username("user2").password("pass2").roles("MANAGER").build();
//
//
//        return new InMemoryUserDetailsManager(admin, manager);
//    }

}
