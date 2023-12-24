package com.javalab.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration  // Bean을 사용한다는 어노테이션
@SuppressWarnings("deprecation")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    // DB password 암호화
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    // http 요청에 대한 보안을 설정한다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성한다
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/main")
                .usernameParameter("username")
                .failureUrl("/user/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
        ;

        // 2. 인가설정(권한)
        http.authorizeRequests()
                // 정적 자원들은 누구나 호출 가능
                .mvcMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/ckeditor2/**", "/vendor/**", "/assets/**","/files/**", "/").permitAll()
                // 컨트롤러의 이미지 제공 매소드는 누구나 호출 가능
                .mvcMatchers("/view/**").permitAll()
                // 로그인, 회원가입 메소드는 누구나 호출 가능
                .mvcMatchers("/user/**","/mail/**","/item/**").permitAll()
                // 게시판은 USER/ADMIN 호출(사용) 가능
                //.mvcMatchers("/board/**").hasAnyRole("USER", "ADMIN")
                // 상품 주문 관련 기능은 USER/ADMIN 호출 가능
                //.mvcMatchers("/item/**").hasAnyRole("USER", "ADMIN")
                // 상품관리(등록/수정/삭제)는 관리자만 이용가능
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/mail/**") // csrf disable 설정
        ;

        // OAuth 설정
        http.oauth2Login()
                .loginPage("/user/login");
        ;
    }





/*    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // super.configure(http) // 이코드 삭제하면 기존 시큐리티가 가진 모든 기능 비활성화
        http.csrf().disable(); // csrf 토큰 비활성화 코드

        http.authorizeRequests()
                //.antMatchers("/main", "/item/**").authenticated() // 이 주소로 시작되면 인증이필요
                .anyRequest().permitAll() // 모든 주소는 인증 필요 없음
                .and()
                .formLogin() // 로그인 페이지와 로그인 성공 시 보내줄 url을 정의
                .loginPage("/signin") // 인증이 필요한 주소로 접속하면 이 주소로 이동시킴
                .loginProcessingUrl("/signin") // 스프링 시큐리티가 로그인 자동 진행 Post 방식으로 로그인 진행
                .defaultSuccessUrl("/main"); // 로그인이 정상적이면 "/" 로 이동
    }*/

}
