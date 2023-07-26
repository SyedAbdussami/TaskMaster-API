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
import java.util.*;

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
        user.setUserRole(Role.NOT_ASSIGNED);
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
        String jwtToken;
        Map<String,Object>map=new HashMap<>();
        map.putIfAbsent("UserId",user.getUserId());
        if(user.getUserStatus()==UserStatus.APPROVED){
            map.put("Roles", Collections.singletonList(user.getUserRole()));
            jwtToken=jwtService.generateToken( map,user);
        }else {
            jwtToken=jwtService.generateToken(user);
        }
        UserDTO userDTO=mapper.map(user,UserDTO.class);
        userDTO.setToken(jwtToken);
        return userDTO;
    }

    public boolean permissionCheck(String token,Role role){
        return jwtService.extractUserRole(token).equals(role);
    }

    public boolean checkJwtExpiration(String token){
        return jwtService.isTokenExpired(token);
    }

}
