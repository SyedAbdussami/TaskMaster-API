package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public boolean userCreatedVerification(User user){
        String userName=user.getUserName();
        if(userRepo.findUserByUserName(userName)==null){
            return false;
        }
        return userRepo.findUserByUserName(userName).getUserName().equals(userName);
    }

    public boolean userCreatedVerificationUsingId(long userId){
        Set<Long> actualUserIds=userRepo.getAllUserIds();
        return actualUserIds.contains(userId);
    }

}
