package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.ProjectDTO;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Project;
import com.taskManager.Tasks.Services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/project/")
public class ProjectController {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    ModelMapper mapper=new ModelMapper();
    @Autowired
    ProjectService projectService;


    @GetMapping
    private ResponseEntity<?> getProjects(){
        List<Project> projectList=projectService.getAllProjects();
        System.out.println("GET Req for All Projects received");
        if(projectList.isEmpty()){
            return new ResponseEntity<>("No projects created yet",HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(projectList, HttpStatus.ACCEPTED);
    }

    @PostMapping
    private ResponseEntity<?> addProject(@RequestBody Project project){
        if(projectService.projectAlreadyExists(project)){
            throw new CustomException("Project Already Exists","Please Contact the admin",HttpStatus.BAD_REQUEST);
        }
        projectService.addProject(project);
        System.out.println("new project added successfully");
        ProjectDTO projectDTO=mapper.map(project,ProjectDTO.class);
        projectDTO.setProjectCreatedDate(dtf.format(LocalDateTime.now()));
        return new ResponseEntity<>(projectDTO,HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/")
    private ResponseEntity<?> updateProject(@RequestBody Project project){
        if(!projectService.projectAlreadyExists(project)){
            throw new CustomException("Project doesn't Exists","Please Contact the admin",HttpStatus.BAD_REQUEST);
        }
        Project project1=projectService.updateProjectDetails(project);
        System.out.println("Project Updated successfully");
        ProjectDTO projectDTO=mapper.map(project1,ProjectDTO.class);
//        projectDTO.setProjectCreatedDate(dtf.format(LocalDateTime.now()));
        return new ResponseEntity<>(projectDTO,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{projectId}")
    private ResponseEntity<?> deleteProject(@RequestParam("name") String projectName,@PathVariable("projectId") long projectId){
        if(!projectService.verifyProjectExistsUsingId(projectId)){
            throw new CustomException("Project titled "+projectName+" does not exist","Try again",HttpStatus.BAD_REQUEST);
        }
        return projectService.deleteProject(projectName,projectId)?new ResponseEntity<>("Project titled deleted",HttpStatus.ACCEPTED):new ResponseEntity<>("Error Occurred try again later",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{projectId}/users/")
    private ResponseEntity<?> includeUsers(@PathVariable("projectId") long projectId,@RequestBody List<UUID> userIds){
        if(!projectService.verifyProjectExistsUsingId(projectId)){
            throw new CustomException("Project does not exist","Try again",HttpStatus.BAD_REQUEST);
        }
        Project project=projectService.assignUsersToProject(projectId,userIds);
        ProjectDTO projectDTO=mapper.map(project,ProjectDTO.class);
        return new ResponseEntity<>(projectDTO,HttpStatus.ACCEPTED);
    }

    @PostMapping("/{projectId}/users/{userId}")
    private ResponseEntity<?> removeUsersFromProject(@PathVariable("projectId") long projectId,@PathVariable("userId") UUID userId){
        if(!projectService.verifyProjectExistsUsingId(projectId)){
            throw new CustomException("Project does not exist","Try again",HttpStatus.BAD_REQUEST);
        }
        Project project=projectService.removeUserFromProject(projectId,userId);
        ProjectDTO projectDTO=mapper.map(project,ProjectDTO.class);
        return new ResponseEntity<>(projectDTO,HttpStatus.ACCEPTED);
    }


}
