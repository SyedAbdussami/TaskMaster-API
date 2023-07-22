package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.Enum.UserStatus;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Services.AuthenticationService;
import com.taskManager.Tasks.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    ModelMapper mapper=new ModelMapper();

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authService;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @PostMapping("/signup")
    private ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        //registerUser
        UserDTO userDTO=authService.registerUser(userRequest);
        System.out.println(userRequest.getUserName()+" sign-ing up ");
        userDTO.setUserStatus(UserStatus.CREATED);
        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    private ResponseEntity<?> loginUser(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(authService.loginUser(userRequest), HttpStatus.ACCEPTED);
    }
    @PostMapping(value = "/user/{userId}")
    private ResponseEntity<?> approveUserRequest(@PathVariable("userId") UUID userId,UserRequest userRequest) {
        if(!authService.checkJwtExpiration(userRequest.getToken())){
            throw new CustomException("User Session Expired","Please login again",HttpStatus.UNAUTHORIZED);
        }
        //Authenticate and log the request
        UserDTO userDTO= userService.approveUserRequest(userId);
        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
//        userDTO.setDateJoined(dtf.format(LocalDateTime.now()));
        return new ResponseEntity<>(userDTO,HttpStatus.ACCEPTED);
    }


}
