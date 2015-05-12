package com.statscollector.gerrit.controller;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.service.GerritConfigTranslator;

@RestController
@RequestMapping("/gerrit/config")
public class GerritConfigRestfulController {

	final static Logger LOGGER = Logger.getLogger(GerritConfigRestfulController.class);

	@Autowired
	private GerritConfig gerritConfig;

	@Autowired
	private GerritConfigTranslator gerritConfigTranslator;

	@RequestMapping(value = "/info", produces = "application/json")
	@ResponseBody
	public GerritConfig gerritInfo() {
		return gerritConfig;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody GerritConfig handleFileUpload(@RequestParam("file") final MultipartFile file)
			throws IOException, ConfigurationException {
		LOGGER.info("Uploaded File: " + file.getName());
		return gerritConfigTranslator.updateConfigFromFile(file, gerritConfig);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeInfo")
	public GerritConfig changeInfo(@RequestParam(required = true) final String host,
			@RequestParam(required = true) final Integer hostPort,
			@RequestParam(required = true) final String username, @RequestParam(required = true) final String password,
			@RequestParam(required = true) final String topicRegex,
			@RequestParam(required = true) final Integer threadSplitSize,
			@RequestParam(required = true) final Integer startDateOffset,
			@RequestParam(required = true) final Integer endDateOffset,
			@RequestParam(required = true) final String projectRegex) {
		try {
			LOGGER.info("Changing Gerrit Host To: " + host);
			gerritConfig.setHost(host);
			LOGGER.info("Changing Gerrit Host Port To: " + hostPort);
			gerritConfig.setHostPort(hostPort);
			LOGGER.info("Changing Gerrit Username & Password To: " + username + ", " + password);
			gerritConfig.setUsernameAndPassword(username, password);
			LOGGER.info("Changing Gerrit Topic To: " + topicRegex);
			gerritConfig.setTopicRegex(topicRegex);
			LOGGER.info("Changing Gerrit Thread Split Size To: " + threadSplitSize);
			gerritConfig.setThreadSplitSize(threadSplitSize);
			LOGGER.info("Changing Start Date Offset To: " + startDateOffset);
			gerritConfig.setStartDateOffset(startDateOffset);
			LOGGER.info("Changing End Date Offset To: " + endDateOffset);
			gerritConfig.setEndDateOffset(endDateOffset);
			LOGGER.info("Changing Project Regex To: " + projectRegex);
		} catch (ConfigurationException e) {
			return gerritConfig;
		}
		return gerritConfig;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveTargets")
	public GerritConfig saveTargets(@RequestParam(required = true) final Float noPeerReviewTarget,
			@RequestParam(required = true) final Float onePeerReviewTarget,
			@RequestParam(required = true) final Float twoPeerReviewTarget,
			@RequestParam(required = true) final Float collaborativeDevelopmentTarget) {
		try {
			LOGGER.info("Changing Gerrit Targets for NoPeer, OnePeer, TwoPeer & Collaborative (respectively) To: "
					+ noPeerReviewTarget + ", " + onePeerReviewTarget + ", " + twoPeerReviewTarget + ", "
					+ collaborativeDevelopmentTarget);
			gerritConfig.setTargets(noPeerReviewTarget, onePeerReviewTarget, twoPeerReviewTarget,
					collaborativeDevelopmentTarget);
		} catch (ConfigurationException e) {
			return gerritConfig;
		}
		return gerritConfig;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addReviewerToIgnore")
	public GerritConfig addReviewer(@RequestParam(required = true) final String reviewer) {
		try {
			LOGGER.info("Changing Reviewer To: " + reviewer);
			gerritConfig.addReviewer(reviewer);
		} catch (ConfigurationException e) {
			return gerritConfig;
		}
		return gerritConfig;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/removeReviewerToIgnore")
	public GerritConfig removeReviewer(@RequestParam(required = true) final String reviewer) {
		try {
			LOGGER.info("Removing Reviewer: " + reviewer);
			gerritConfig.removeReviewer(reviewer);
		} catch (ConfigurationException e) {
			return gerritConfig;
		}
		return gerritConfig;
	}

}
