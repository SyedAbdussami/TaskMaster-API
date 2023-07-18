package com.taskManager.Tasks.Services;


import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Enum.UserStatus;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.UserRepo;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Security.JwtService;
import com.taskManager.Tasks.Security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    ModelMapper mapper=new ModelMapper();

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    public UserDTO registerUser(UserRequest userRequest){
        userRequest.setDateJoined(dtf.format(LocalDateTime.now()));
        User user=mapper.map(userRequest,User.class);
        if(userService.userCreatedVerification(user)){
//            System.out.println("User already exits");
//            return;
            throw new CustomException("User ","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
        user.setUserStatus(UserStatus.CREATED);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        user.setUserId(userRepo.findUserByUserName(userRequest.getUserName()).getUserId());
        return mapper.map(userRepo.findUserByUserName(userRequest.getUserName()),UserDTO.class);
    }

    public UserDTO loginUser(UserRequest userRequest){
        String userName= userRequest.getUserName();
        String password=userRequest.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName,password)
        );
        User user=userRepo.findUserByUserName(userName);
//        Map<String, List<Role>>map=new HashMap<>();
//        map.put("Roles", Collections.singletonList(Role.USER_ROLE));
        String jwtToken=jwtService.generateToken( user);
        UserDTO userDTO=mapper.map(user,UserDTO.class);
        userDTO.setToken(jwtToken);
        return userDTO;
    }
}
