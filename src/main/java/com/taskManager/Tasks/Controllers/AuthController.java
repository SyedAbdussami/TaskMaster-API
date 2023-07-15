package com.taskManager.Tasks.Controllers;


import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    ModelMapper mapper=new ModelMapper();

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    private ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        //registerUser
        UserDTO userDTO=userService.registerUser(userRequest);
        System.out.println(userRequest.getUserName()+" sign-ing up ");
        userDTO.setApprovalStatus("User Created pending approval. Please login to get status");
        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }


}
