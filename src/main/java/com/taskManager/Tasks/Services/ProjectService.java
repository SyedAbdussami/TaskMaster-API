package com.taskManager.Tasks.Services;


import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Project;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    UserService userService;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void addProject(Project project){
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
        //mapping userids to users
        projectRepo.save(project);
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
}
