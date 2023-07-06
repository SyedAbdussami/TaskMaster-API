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
//    private ResponseEntity<?> createUser(@RequestBody User user){
//        userService.addUser(user);
//        return new ResponseEntity<>(user.getUserName()+" created ",HttpStatus.CREATED);
//    }

    @PostMapping
    private UserDTO createUser(@RequestBody User user) {
        user.setDateJoined(dtf.format(LocalDateTime.now()));
        userService.addUser(user);
//        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
        return mapper.map(user, UserDTO.class);
    }

    @PutMapping(value = "/{userId}")
    private UserDTO updateUser(@RequestBody UserRequest userRequest, @PathVariable UUID userId){
        return mapper.map(userService.updateUser(userRequest,userId),UserDTO.class);
    }



    //update user using username/first/lastname
}
