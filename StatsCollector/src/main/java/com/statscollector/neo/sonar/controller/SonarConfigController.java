package com.statscollector.neo.sonar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sonar/config")
public class SonarConfigController {

	@RequestMapping("/view")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("sonar/sonarConfig", model);
	}

}