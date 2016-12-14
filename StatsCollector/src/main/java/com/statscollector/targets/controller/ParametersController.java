package com.statscollector.targets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.targets.dao.GerritDisplayParametersRepository;
import com.statscollector.targets.dao.SonarDisplayParametersRepository;
import com.statscollector.targets.model.DisplayParameters;
import com.statscollector.targets.model.GerritDisplayParameters;
import com.statscollector.targets.model.SonarDisplayParameters;

@RestController
public class ParametersController {

    @Autowired
    private GerritDisplayParametersRepository gerritDisplayParametersRepository;

    @Autowired
    private SonarDisplayParametersRepository sonarDisplayParametersRepository;

    @RequestMapping(value = "/parameters/{projectName}", method = RequestMethod.GET)
    @ResponseBody
    public DisplayParameters parameters(@PathVariable final String projectName) {
        return new DisplayParameters(gerritDisplayParametersRepository.findByProjectName(projectName),
                sonarDisplayParametersRepository.findByProjectName(projectName));
    }

    @RequestMapping(value = "/parameters/save", method = RequestMethod.POST)
    @ResponseBody
    public DisplayParameters parameters(@RequestBody final DisplayParameters displayParameters) {
        GerritDisplayParameters gerritParams = gerritDisplayParametersRepository
                .save(displayParameters.getGerritDisplayParameters());
        SonarDisplayParameters sonarParams = sonarDisplayParametersRepository
                .save(displayParameters.getSonarDisplayParameters());
        return new DisplayParameters(gerritParams, sonarParams);
    }

}
