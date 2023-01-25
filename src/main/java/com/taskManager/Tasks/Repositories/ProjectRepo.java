package com.taskManager.Tasks.Repositories;

import com.taskManager.Tasks.Models.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends CrudRepository<Project,Long> {

    Project findProjectByProjectName(String name);
}
