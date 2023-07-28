package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.RequestModels.TaskRequest;
import com.taskManager.Tasks.RequestModels.TaskWorkRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("api/project")
public class TaskController {

    @Autowired
    TaskService taskService;
    @GetMapping("/tasks")
    private ResponseEntity<?> getAllTasks(){
        List<TaskDTO> taskDTOS=taskService.getAllTasks();
        if(taskDTOS.isEmpty()){
            return new ResponseEntity<>("No tasks created yet",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(taskDTOS, HttpStatus.ACCEPTED);
    }

    @PostMapping("/{projectId}/tasks")
    private ResponseEntity<?> createTask(@PathVariable("projectId") long projectId,@RequestBody TaskRequest taskRequest,@RequestHeader("Authorization") String token){
       TaskDTO taskDTO= taskService.addTask(projectId,taskRequest,token.substring(7));
        return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
    }

    @PostMapping("/{projectId}/tasks/{taskId}")
    private ResponseEntity<?> assignUserTask(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId, @RequestBody UserRequest userRequest){
        TaskDTO taskDTO=taskService.assignUserTask(projectId,taskId,userRequest);
        return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/{userId}/work/{taskId}")
    private ResponseEntity<?> taskWorkUpdateByUser(@PathVariable("taskId") long taskId, TaskWorkRequest taskWorkRequest, @PathVariable("userId") UUID userId){
        TaskDTO taskDTO= taskService.taskWorkUpdate(taskId,taskWorkRequest,userId);
        return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    private ResponseEntity<?> deleteTask(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId,@RequestHeader("Authorization") String token){
        if(taskService.deleteTask(taskId,projectId,token.substring(7))){
            return new ResponseEntity<>("Task Successfully Deleted",HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Task cannot be deleted. Contact the admin or your Manager",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{projectId}/tasks/{taskId}")
    private ResponseEntity<?> updateTaskDetails(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId,@RequestHeader("Authorization") String token,@RequestBody TaskRequest taskRequest){
        TaskDTO taskDTO=taskService.updateTaskDetails(taskId,projectId,taskRequest,token.substring(7));
        return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);

    }
    @PostMapping("/{projectId}/tasks/{taskId}/users/remove")
    private ResponseEntity<?> removeUserFromTask(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId,@RequestHeader("Authorization") String token,@RequestBody TaskRequest taskRequest){
        TaskDTO taskDTO=taskService.removeUsersFromTask(taskId,taskRequest.getUsers(),token);
        return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
    }

   @PostMapping("/{projectId}/tasks/{taskId}/status")
   private ResponseEntity<?> changeTaskStatus(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token,@RequestBody TaskRequest taskRequest){
        TaskDTO taskDTO=taskService.updateTaskStatus(taskId,taskRequest,token);
       return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
   }
}
