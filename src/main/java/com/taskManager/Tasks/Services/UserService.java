package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import com.taskManager.Tasks.Repositories.TaskRepo;
import com.taskManager.Tasks.Repositories.UserRepo;
import com.taskManager.Tasks.RequestModels.TaskWorkRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;

    ModelMapper mapper=new ModelMapper();

    public void addUser(User user){
        if(userCreatedVerification(user)){
//            System.out.println("User already exits");
//            return;
            throw new CustomException("User Already Exists","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
        //admin token verification
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
    public User updateUser(UserRequest userRequest, UUID userId){
        if(getUserById(userId)==null){
            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
//        if(userRepo.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName())==null){
//            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
//        }
        //admin token verification

        User user=getUserById(userId);
        user.setUserId(userId);
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUserOccupation(userRequest.getUserOccupation());
        user.setUserStatus(userRequest.getUserStatus());
        userRepo.save(user);
        return getUserById(userId);
    }


    public User updateUserName(UserRequest userRequest, UUID userId){
        if(getUserById(userId)==null){
            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
        }
//        if(userRepo.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName())==null){
//            throw new CustomException("User does not exist","Please contact the admin", HttpStatus.BAD_REQUEST);
//        }
        //admin
        User user=getUserById(userId);
        user.setUserId(userId);
        user.setUserName(userRequest.getUserName());
        userRepo.save(user);
        return getUserById(userId);
    }

    public boolean updateUserDetails(User modifiedUser){
        if(!userCreatedVerificationUsingId(modifiedUser.getUserId())){
            throw new CustomException("User for the given info does not exists","Pls contact admin",HttpStatus.BAD_REQUEST);
        }
        userRepo.save(modifiedUser);
        return true;
    }

    public List<Long> fetchUserProjects(User user){

        return projectRepo.findProjectsByUserId(user.getUserId());
    }

    public boolean deleteUser(UUID userId,String userName){
        if(!userCreatedVerificationUsingId(userId)){
            throw new CustomException("User for the given info does not exists","Pls contact admin",HttpStatus.NOT_FOUND);
        }
        User fetchedUser=getUserById(userId);
        if(!fetchedUser.getUserName().equals(userName)){
            throw new CustomException("Please verify the provided username is correct","Didn't Match with our records",HttpStatus.NOT_FOUND);
        }
        userRepo.delete(fetchedUser);
        return true;
    }

    public boolean verifyUsersCreatedUsingId(List<UUID> userIds){
        for(UUID userId:userIds){
            if (!userCreatedVerificationUsingId(userId)){
                return false;
            }
        }
        return true;
    }

    public List<User> getUsersByIds(List<UUID> userIds){
        if(!verifyUsersCreatedUsingId(userIds)){
            throw new CustomException("Users from the provided user list do not exist","Enter the correct id's",HttpStatus.NOT_FOUND);
        }
        List<User> userList=new ArrayList<>();
        for(UUID userId:userIds){
            User user=getUserById(userId);
            userList.add(user);
        }
        return userList;
    }

}
