package com.statscollector.neo.sonar.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.statscollector.neo.sonar.model.SonarProject;

/**
 * I am the repository for accessing SonarProjects.
 *
 * @author JCannon
 *
 */
@Repository
public interface SonarProjectRepository extends CrudRepository<SonarProject, Long> {

    @Override
    public List<SonarProject> findAll();

    /**
     * Find SonarProject by passed in name.
     *
     * @param name
     * @return
     */
    public SonarProject findByName(String name);

}
