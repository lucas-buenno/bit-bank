package com.fbr.tech.BitBank.filter;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfiguration {

    private final IpFilter ipFilter;
    
    @Bean
    public FilterRegistrationBean<IpFilter> filterRegistrationBean() {

        var registrationBean = new FilterRegistrationBean<IpFilter>();

         registrationBean.setFilter(ipFilter);
         registrationBean.setOrder(0);
         return registrationBean;
     }
}
