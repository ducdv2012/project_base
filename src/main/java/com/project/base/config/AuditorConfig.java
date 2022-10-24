//package com.project.base.config;
//
//import com.project.base.service.impl.AuditorAwareImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@Configuration
//@EnableWebMvc
//@EnableWebSecurity
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//public class AuditorConfig {
//    @Bean
//    public AuditorAware<Long> auditorAware() {
//        return new AuditorAwareImpl();
//    }
//}
