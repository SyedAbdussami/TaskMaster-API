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
        userService.addUser(user);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
        return userDTO;
    }


}
