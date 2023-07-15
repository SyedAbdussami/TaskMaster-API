package com.taskManager.Tasks.Services;


import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Project;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    UserService userService;
    ModelMapper mapper=new ModelMapper();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public long addProject(Project project){
        if (project.getUserIds().isEmpty()){
            project.setCreatedAt(dtf.format(LocalDateTime.now()));
            projectRepo.save(project);
            return projectRepo.findProjectIdByProjectName(project.getProjectName());
        }
        List<UUID> claimedUsersIds=project.getUserIds();
        List<User> claimedUsers=new ArrayList<>();
        for (UUID userId:claimedUsersIds) {
            if(!userService.userCreatedVerificationUsingId(userId)){
                throw new CustomException("User Does not exist","please try creating the user", HttpStatus.BAD_REQUEST);
            }
            claimedUsers.add(userService.getUserById(userId));
        }
        project.setCreatedAt(dtf.format(LocalDateTime.now()));
        project.setUsers(claimedUsers);
        project.setUserIds(claimedUsers.stream().map(User::getUserId).collect(Collectors.toList()));
        //mapping userids to users
        projectRepo.save(project);
        return projectRepo.findProjectIdByProjectName(project.getProjectName());
    }

    public  List<Project> getAllProjects(){
        return (List<Project>) projectRepo.findAll();
    }
    public boolean projectAlreadyExists(Project project){
        String projectName=project.getProjectName();
        if(projectRepo.findProjectByProjectName(projectName)==null){
            return false;
        }
        return projectRepo.findProjectByProjectName(projectName).getProjectName().equals(projectName);
    }

    public boolean verifyProjectExistsUsingId(long projectId){
        Set<Long> projectIds= projectRepo.getAllProjectIds();
        return projectIds.contains(projectId);
    }
    public Project getProjectUsingID(long projectId){
        if(!verifyProjectExistsUsingId(projectId)){
            throw new CustomException("Project does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
        return projectRepo.getProjectByProjectId(projectId);
    }

    public Project updateProjectDetails(Project project){
        Project oldProject=getProjectUsingID(project.getProjectId());
        oldProject.setProjectDescription(project.getProjectDescription());
        oldProject.setProjectName(project.getProjectName());
        projectRepo.save(oldProject);
        return oldProject;
    }
    public boolean deleteProject(String projectName,long projectId ){
        if(!(projectRepo.findProjectByProjectName(projectName)==null&&verifyProjectExistsUsingId(projectId))){
            return false;
        }
        Project project=projectRepo.getProjectByProjectId(projectId);
        projectRepo.delete(project);
        return projectRepo.findProjectByProjectName(projectName) == null && !verifyProjectExistsUsingId(projectId);
    }

    public Project assignUsersToProject(long projectId,List<UUID> userIDs){
        Project project=projectRepo.getProjectByProjectId(projectId);
        List<User> users=new ArrayList<>();
        for(UUID id:userIDs){
            if(!userService.userCreatedVerificationUsingId(id)){
                throw new CustomException("User with id" +id+"does not exist. Please try again","",HttpStatus.BAD_REQUEST);
            }
            users.add(userService.getUserById(id));
        }
        project.setUsers(users);
        return project;
    }

    public Project removeUserFromProject(long projectId,UUID userId){
        Project project=projectRepo.getProjectByProjectId(projectId);
        if(userService.userCreatedVerificationUsingId(userId)){
            throw new CustomException("User does not exists.","",HttpStatus.BAD_REQUEST);
        }
        List<User> users=project.getUsers();
        User userToBeDeleted=userService.getUserById(userId);
        users.remove(userToBeDeleted);
        return project;
    }

    public boolean checkProjectAccessPrivilege(UUID userId){
        User user=userService.getUserById(userId);
        return user.getUserRole().equals(Role.valueOf("ROLE_ADMIN"));
    }


}
