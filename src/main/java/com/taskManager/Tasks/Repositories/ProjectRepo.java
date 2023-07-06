package com.taskManager.Tasks.Repositories;

import com.taskManager.Tasks.Models.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProjectRepo extends CrudRepository<Project,Long> {

    Project findProjectByProjectName(String name);

    @Query(value = "select projectId from Project")
    Set<Long> getAllProjectIds();

    Project getProjectByProjectId(long projectId);

    @Query("SELECT p.projectId FROM Project p JOIN p.users u WHERE u.userId = :userId")
    List<Long> findProjectsByUserId(@Param("userId") UUID userId);

    @Query("select projectId from Project where projectName=:projectName")
    Long findProjectIdByProjectName(@Param("projectName") String projectName);
}
