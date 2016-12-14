package com.statscollector.targets.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.statscollector.targets.model.SonarDisplayParameters;

public interface SonarDisplayParametersRepository extends CrudRepository<SonarDisplayParameters, Long> {

    @Override
    public List<SonarDisplayParameters> findAll();

    /**
     * Find SonarProject by passed in name.
     *
     * @param name
     * @return
     */
    public SonarDisplayParameters findByProjectName(String projectName);

}
