package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import com.taskManager.Tasks.Repositories.TaskRepo;
import com.taskManager.Tasks.Repositories.UserRepo;
import com.taskManager.Tasks.RequestModels.TaskRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    ModelMapper mapper=new ModelMapper();

     public TaskDTO addTask(long projectId,TaskRequest taskRequest){
         Task task=mapper.map(taskRequest,Task.class);
         if(!projectService.verifyProjectExistsUsingId(projectId)&&!userService.verifyUsersCreatedUsingId(taskRequest.getUsers())){
             throw new CustomException("Please verify the project name or the list of user Ids","", HttpStatus.BAD_REQUEST);
         }
        taskRepo.save(task);
        TaskDTO taskDTO=mapper.map(task,TaskDTO.class);
        taskDTO.setCreatedAt(dtf.format(LocalDateTime.now()));
        taskDTO.setAssignedUsersIds(taskRequest.getUsers());
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks(){
         List<Task> tasks= (List<Task>) taskRepo.findAll();
         List<TaskDTO> taskDTOS=new ArrayList<>();
         for(Task task:tasks){
             taskDTOS.add(mapper.map(task,TaskDTO.class));
         }
         return taskDTOS;
    }

    public Task getTaskById(long taskId){
         return taskRepo.getTaskByTaskId(taskId);
    }

    public TaskDTO assignUserTask(long projectId, long taskId, UserRequest userRequest){
         if(!verifyTaskCreated(taskId)&&!projectService.verifyProjectExistsUsingId(projectId)&&!userService.userCreatedVerificationUsingId(userRequest.getUserId())){
             throw new CustomException("Please check task ID or Project Id or User ID","Contact the admin",HttpStatus.BAD_REQUEST);
         }
         Task task=taskRepo.getTaskByTaskId(taskId);
         List<User> users=task.getUsers();
         User newUser=userService.getUserById(userRequest.getUserId());
         users.add(newUser);
         task.setUsers(users);

         taskRepo.save(task);

         TaskDTO updatedTaskDTO=mapper.map(task,TaskDTO.class);
         updatedTaskDTO.setAssignedUsersIds(userService.fetchUserIdsByTask(taskId));
         return updatedTaskDTO;
    }

    public boolean verifyTaskCreated(long taskId){
         return taskRepo.findById(taskId).isPresent();
    }

}
