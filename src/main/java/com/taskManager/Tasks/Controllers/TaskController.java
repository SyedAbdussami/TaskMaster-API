package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.DTOs.TaskWorkDTO;
import com.taskManager.Tasks.Enum.TaskPriority;
import com.taskManager.Tasks.Enum.TaskStatus;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.RequestModels.TaskRequest;
import com.taskManager.Tasks.RequestModels.TaskWorkRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private ResponseEntity<?> assignUserTask(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId, @RequestBody UserRequest userRequest,@RequestHeader("Authorization")String token){
        TaskDTO taskDTO=taskService.assignUserTask(projectId,taskId,userRequest,token.substring(7));
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
        TaskDTO taskDTO=taskService.removeUsersFromTask(taskId,taskRequest.getUsers(),token.substring(7));
        return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
    }

   @PostMapping("/{projectId}/tasks/{taskId}/status")
   private ResponseEntity<?> changeTaskStatus(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token,@RequestBody TaskRequest taskRequest){
        TaskDTO taskDTO=taskService.updateTaskStatus(taskId,taskRequest,token.substring(7));
       return new ResponseEntity<>(taskDTO,HttpStatus.ACCEPTED);
   }

   @GetMapping("/tasks/{priority}")
   private ResponseEntity<?> getAllTasksWithPriority(@PathVariable("priority") String taskPriority,@RequestHeader("Authorization") String token){
        List<TaskDTO> taskDTOS=taskService.getTasksWithPriority(taskPriority,token.substring(7));
        return new ResponseEntity<>(taskDTOS,HttpStatus.ACCEPTED);
   }
   @PutMapping("/{projectId}/tasks/{taskId}/{priority}")
    private ResponseEntity<?> changeTaskPriority(@PathVariable("projectId") long projectId,@PathVariable("taskId") long taskId,@PathVariable("priority") String taskPriority,@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(taskService.changeTaskPriority(taskId,taskPriority,token.substring(7)),HttpStatus.ACCEPTED);
   }

   @GetMapping("/{projectId}/tasks/{taskId}/users")
    private ResponseEntity<?> getUsersAssignedToTask(@PathVariable("projectId") long projectId,@PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token){
        return new ResponseEntity<>(taskService.getUsersAssignedToTask(taskId,token.substring(7)),HttpStatus.ACCEPTED);
   }

   @GetMapping("/{projectId}/tasks/{taskId}/work")
    private ResponseEntity<?> getAllWorkDoneOnTask(@PathVariable("projectId") long projectId,@PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token){
        return new ResponseEntity<>(taskService.getAllWorkDoneOnTask(taskId,token.substring(7)),HttpStatus.OK);
   }
   @PostMapping("/{projectId}/tasks/{taskId}/work")
    private ResponseEntity<?> addWorkToTask(@PathVariable("projectId") long projectId, @PathVariable("taskId") long taskId, @RequestBody TaskWorkRequest taskWorkRequest, @RequestParam("File")MultipartFile file,@RequestHeader("Authorization") String token) throws IOException {
        return new ResponseEntity<>(taskService.createWorkOnTask(taskId,taskWorkRequest,file,token.substring(7)),HttpStatus.ACCEPTED);
   }
   @GetMapping("/{projectId}/tasks/{taskId}/work/{taskWorkId}")
    private ResponseEntity<?>getWorkDoneOnTask(@PathVariable("projectId") long projectId,@PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token,@PathVariable("taskWorkId") long taskWorkId){
        return new ResponseEntity<>(taskService.getWorkDoneOnTask(taskId,taskWorkId,token.substring(7)),HttpStatus.OK);
   }

   @PostMapping("/{projectId}/tasks/{taskId}/work/{taskWorkId}/approve")
    private ResponseEntity<?>approveTaskWork(@PathVariable("projectId") long projectId,@PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token,@PathVariable("taskWorkId") long taskWorkId){
        return new ResponseEntity<>(taskService.approveTaskWork(taskId,taskWorkId,token.substring(7)),HttpStatus.ACCEPTED);
   }

    @PostMapping("/{projectId}/tasks/{taskId}/work/{taskWorkId}/reject")
    private ResponseEntity<?>rejectTaskWork(@PathVariable("projectId") long projectId,@PathVariable("taskId") long taskId,@RequestHeader("Authorization")String token,@PathVariable("taskWorkId") long taskWorkId){
        return new ResponseEntity<>(taskService.rejectTaskWork(taskId,taskWorkId,token.substring(7)),HttpStatus.ACCEPTED);
    }
}
