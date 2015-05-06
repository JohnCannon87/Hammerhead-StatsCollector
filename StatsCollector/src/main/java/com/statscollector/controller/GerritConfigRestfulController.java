package com.statscollector.controller;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.config.GerritConfig;

@RestController
@RequestMapping("/gerrit/config")
public class GerritConfigRestfulController {

	private static final String FAILURE_STRING = "Error Change Not Made: ";
	private static final String SUCCESS_STRING = "OK";
	@Autowired
	private GerritConfig gerritConfig;

	@RequestMapping(value = "/info", produces = "application/json")
	@ResponseBody
	public GerritConfig gerritInfo() {
		return gerritConfig;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeHost")
	public String changeHost(@RequestParam(required = true) final String host) {
		try {
			gerritConfig.setHost(host);
		} catch (ConfigurationException e) {
			return FAILURE_STRING + e.getMessage();
		}
		return SUCCESS_STRING;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeReviewersToIgnore")
	public String changeReviewersToIgnore(@RequestParam(required = true) final List<String> reviewersToIgnore) {
		try {
			gerritConfig.setReviewersToIgnore(reviewersToIgnore);
		} catch (ConfigurationException e) {
			return FAILURE_STRING + e.getMessage();
		}
		return SUCCESS_STRING;
	}
}
