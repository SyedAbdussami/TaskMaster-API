package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.Enum.Role;
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
//            if(user.getUserRole().equals(Role.USER_ADMIN)){
//                continue;
//            }
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

    @PostMapping(value = "/{userId}")
    private ResponseEntity<?> approveUserRequest(@PathVariable("userId") UUID userId) {

        //Authenticate and log the request
       UserDTO userDTO= userService.approveUserRequest(userId);
        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
//        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
        return new ResponseEntity<>(userDTO,HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/{userId}")
    private ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest, @PathVariable UUID userId){
        return new ResponseEntity<>(mapper.map(userService.updateUser(userRequest,userId),UserDTO.class),HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/{userId}/approve")
    private ResponseEntity<?> approveUser(@PathVariable("userId") UUID userId,@RequestBody UserRequest userRequest){
        if(userService.approveUser(userId,userRequest)){
            return new ResponseEntity<>(mapper.map(userService.getUserById(userId),UserDTO.class),HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Unable to approve the created user. Please Try again",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{userId}")
    private ResponseEntity<?> deleteUser(@PathVariable("userId")UUID userId,@RequestBody UserRequest userRequest){
        if(!userService.deleteUser(userId,userRequest.getUserName())){
            return new ResponseEntity<>("Unable to delete User",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("User with id "+userId+" deleted",HttpStatus.ACCEPTED) ;
    }




    //update user using username/first/lastname
}
