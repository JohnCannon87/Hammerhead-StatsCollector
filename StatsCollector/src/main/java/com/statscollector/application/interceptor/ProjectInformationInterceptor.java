package com.statscollector.application.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.statscollector.gerrit.config.GerritConfig;

public class ProjectInformationInterceptor extends HandlerInterceptorAdapter {

	private static final String PROJECT_NAME_KEY = "projectName";
	@Autowired
	private GerritConfig gerritConfig;

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView model) {
		if (model != null) {
			model.addObject(PROJECT_NAME_KEY, gerritConfig.getProjectName());
		}
	}
}
