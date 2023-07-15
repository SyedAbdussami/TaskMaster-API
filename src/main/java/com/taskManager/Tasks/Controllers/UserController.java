package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.RequestModels.TaskWorkRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    ModelMapper mapper=new ModelMapper();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @GetMapping
    private ResponseEntity<?> getUsers(){
        List<User> users=userService.getAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<>("No users created yet",HttpStatus.ACCEPTED);
        }
        List<UserDTO> userDTOS =new ArrayList<>();
        for(User user:users){
            UserDTO usr1=mapper.map(user,UserDTO.class);
            usr1.setProjectIds(userService.fetchUserProjects(user));
            userDTOS.add(usr1);
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.ACCEPTED);
    }
//    @PostMapping
//    private ResponseEntity<?> createUser(@RequestBody UserRequest userRequest){
//        userService.approveUserRequest(userRequest);
//        return new ResponseEntity<>(userRequest.getUserName()+" created ",HttpStatus.CREATED);
//    }

    @PostMapping
    private ResponseEntity<?> approveUserRequest(@RequestBody UserRequest user) {
        user.setDateJoined(dtf.format(LocalDateTime.now()));
        userService.approveUserRequest(user);
//        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
        return new ResponseEntity<>(mapper.map(user, UserDTO.class),HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/{userId}")
    private ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest, @PathVariable UUID userId){
        return new ResponseEntity<>(mapper.map(userService.updateUser(userRequest,userId),UserDTO.class),HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/{userId}/approve")
    private ResponseEntity<?> approveUser(@PathVariable("userId") UUID userId){
        if(userService.approveUser(userId)){
            return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Unable to approve the created user. Please Try again",HttpStatus.INTERNAL_SERVER_ERROR);
    }



    //update user using username/first/lastname
}
