package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Services.UserService;
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
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
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
    private UserDTO updateUser(@RequestBody User user,@PathVariable UUID userId){
        return mapper.map(userService.updateUser(user,userId),UserDTO.class);
    }

    //update user using username/first/lastname
}
