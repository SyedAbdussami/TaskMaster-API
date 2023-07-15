package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Enum.TaskStatus;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import com.taskManager.Tasks.Repositories.TaskRepo;
import com.taskManager.Tasks.Repositories.UserRepo;
import com.taskManager.Tasks.RequestModels.TaskRequest;
import com.taskManager.Tasks.RequestModels.TaskWorkRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
         updatedTaskDTO.setAssignedUsersIds(fetchUserIdsByTask(taskId));
         return updatedTaskDTO;
    }

    public boolean verifyTaskCreated(long taskId){
         return taskRepo.findById(taskId).isPresent();
    }
    public TaskDTO updateTaskDetails(long taskId,TaskRequest taskRequest){
         Task oldTask=taskRepo.getTaskByTaskId(taskId);
         Task newTask=mapper.map(taskRequest,Task.class);
         if(Objects.equals(newTask.getTaskName(), oldTask.getTaskName())){
             newTask.setTaskId(oldTask.getTaskId());
         }
         taskRepo.save(newTask);
        return mapper.map(newTask,TaskDTO.class);
    }

    public boolean deleteTask(long taskId){
         Task task=taskRepo.getTaskByTaskId(taskId);
         taskRepo.delete(task);
        return taskRepo.getTaskByTaskId(taskId) == null;
    }

    public TaskDTO updateTaskStatus(long taskId,UUID userID,TaskRequest taskRequest){
         if(!verifyTaskCreated(taskId)&&!userService.userCreatedVerificationUsingId(userID)){
             throw new CustomException("Task or user does not exist","Try again later",HttpStatus.BAD_REQUEST);
         }
         Task task=taskRepo.getTaskByTaskId(taskId);
        TaskStatus oldStatus=task.getTaskStatus();
        TaskStatus newStatus=taskRequest.getTaskStatus();
        if(oldStatus.equals(newStatus)){
            throw new CustomException("Status can not be changed","Requested status change is same as current task status",HttpStatus.BAD_REQUEST);
        }
        task.setTaskStatus(newStatus);
        taskRepo.save(task);
        return mapper.map(task,TaskDTO.class);
    }

    public TaskDTO removeUsersFromTask(long taskId,List<UUID> userIds){
         if(!verifyTaskCreated(taskId)&&!userService.verifyUsersCreatedUsingId(userIds)){
             throw new CustomException("Either task or list of users Id's are not correct","Please try again correctly or contact the admin",HttpStatus.BAD_REQUEST);
         }
         Task task=taskRepo.getTaskByTaskId(taskId);
         List<UUID> actualUsers= task.getUsers().stream().map(User::getUserId).toList();
//         List<UUID> updatedUserList= task.getUsers().stream().filter(user. -> !userIds.contains(user.getUserId())).toList();
        List<UUID> updatedUserList=actualUsers.stream().filter(userIds::contains).toList();
        task.setUsers(userService.getUsersByIds(updatedUserList));
        taskRepo.save(task);
        return mapper.map(task,TaskDTO.class);
    }

    public TaskDTO taskWorkUpdate(long taskId, TaskWorkRequest taskWorkRequest, UUID userId){
        if(verifyTaskCreated(taskId)){
            throw new CustomException("Given task does not exist","Please try again with a valid task ID",HttpStatus.NOT_FOUND);
        }
        if(userService.userCreatedVerificationUsingId(userId)){
            throw new CustomException("Given task does not exist","Please try again with a valid task ID",HttpStatus.NOT_FOUND);
        }

        //Admin token check
        Task task=getTaskById(taskId);
        long projectId=task.getProject().getProjectId();
        //Manager approval
        return mapper.map(task,TaskDTO.class);

    }

    public List<UUID> fetchUserIdsByTask(long taskIds){
        Task task= getTaskById(taskIds);
        return task.getUsers().stream().map(User::getUserId).toList();
    }

    public long getProjectIdFromTaskId(long taskId){
        if(!verifyTaskCreated(taskId)){
            throw new CustomException("Task associated with task Id does not exist","Try again or contact the admin",HttpStatus.NOT_FOUND);
        }
        Task task=getTaskById(taskId);
        return task.getProject().getProjectId();
    }

    public boolean checkTaskAccessPrivilege(UUID userId){
        User user=userService.getUserById(userId);
        return !user.getUserRole().equals(Role.valueOf("ROLE_ADMIN"));
    }

}
