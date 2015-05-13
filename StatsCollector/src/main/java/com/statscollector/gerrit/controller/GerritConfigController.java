package com.statscollector.gerrit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gerrit/config")
public class GerritConfigController {

	@RequestMapping("/view")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("gerrit/gerritConfig", model);
	}

}