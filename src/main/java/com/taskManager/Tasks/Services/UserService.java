package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public void addUser(User user){
        if(userCreatedVerification(user)){
//            System.out.println("User already exits");
//            return;
            throw new CustomException("User Already Exists","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
        userRepo.save(user);
    }
    public List<User> getAllUsers(){
        return (List<User>) userRepo.findAll();
    }

    public User getUserById(UUID userId){
        return  userRepo.findUsersByUserId(userId);
    }

    public boolean userCreatedVerification(User user){
        String userName=user.getUserName();
        if(userRepo.findUserByUserName(userName)==null){
            return false;
        }
        return userRepo.findUserByUserName(userName).getUserName().equals(userName);
    }

    public boolean userCreatedVerificationUsingId(UUID userId){
        Set<UUID> actualUserIds=userRepo.getAllUserIds();
        return actualUserIds.contains(userId);
    }

    //complete user update.
    public User updateUser(User user, UUID userId){
        if(getUserById(userId)==null){
            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
//        if(userRepo.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName())==null){
//            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
//        }
        user.setUserId(userId);
        user.setDateJoined(getUserById(userId).getDateJoined());
        userRepo.save(user);
        return getUserById(userId);
    }


    @PutMapping
    public User updateUserName(User user, UUID userId){
        if(getUserById(userId)==null){
            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
//        if(userRepo.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName())==null){
//            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
//        }
        user.setUserId(userId);
        user.setDateJoined(getUserById(userId).getDateJoined());
        userRepo.save(user);
        return getUserById(userId);
    }

}
