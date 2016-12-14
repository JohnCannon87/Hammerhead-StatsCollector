package com.statscollector.targets.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.statscollector.targets.model.GerritDisplayParameters;

public interface GerritDisplayParametersRepository extends CrudRepository<GerritDisplayParameters, Long> {

    @Override
    public List<GerritDisplayParameters> findAll();

    /**
     * Find SonarProject by passed in name.
     *
     * @param name
     * @return
     */
    public GerritDisplayParameters findByProjectName(String projectName);

}
