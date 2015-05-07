package com.statscollector.information.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class InformationController {

	private static final String REDIRECT_TO_HOME = "redirect:/home";

	@RequestMapping("/")
	public String index() {
		return REDIRECT_TO_HOME;
	}

	@RequestMapping("/home")
	public ModelAndView home(final ModelMap model) {

		return new ModelAndView("home", model);
	}

}