package com.statscollector.application;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.statscollector.application.interceptor.ProjectInformationInterceptor;

@Configuration
public class WebConfiguration extends WebMvcAutoConfigurationAdapter {

	@Bean
	public ProjectInformationInterceptor projectInformationInterceptor() {
		return new ProjectInformationInterceptor();
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(projectInformationInterceptor());
	}

}
