package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.DTOs.UserDTO;
import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Enum.UserStatus;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import com.taskManager.Tasks.Repositories.UserRepo;
import com.taskManager.Tasks.RequestModels.UserRequest;
import com.taskManager.Tasks.Security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;
//
//    @Autowired
//    TaskService taskService;

    ModelMapper mapper=new ModelMapper();

    @Autowired
    JwtService jwtService;

    public UserDTO approveUserRequest(UUID userId){
        //Future:Authenticate requesting user and log the approval request
        User user=getUserById(userId);
        if(user.getUserStatus()==UserStatus.APPROVED){
            throw new CustomException("User already approved","Try again for another user or contact the admin", HttpStatus.BAD_REQUEST);
        }
        user.setUserStatus(UserStatus.PENDING);
        userRepo.save(user);
        return mapper.map(user,UserDTO.class);
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
    public User updateUser(UserRequest userRequest, UUID userId,String token){
        if(!checkUserIsAdmin(token)){
            throw new CustomException("You don't have the necessary permissions to complete this task","contact the admin",HttpStatus.BAD_REQUEST);
        }
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

    public boolean deleteUser(UUID userId,String token){
        if(!checkRole(token,Role.USER_ADMIN)){
            throw new CustomException("You don't have the necessary permissions to complete this task","contact the admin",HttpStatus.BAD_REQUEST);
        }
        if(!userCreatedVerificationUsingId(userId)){
            throw new CustomException("User for the given info does not exists","Pls contact admin",HttpStatus.NOT_FOUND);
        }
        User fetchedUser=getUserById(userId);

        //Need to send a message to taskservice to de-assign users. Creating ApplicationEvent

        //updating tasks assigned to the users
//        List<Task> userAssignedTasks=getAllTasksAssignedToUser(userId);
//        for(Task task:userAssignedTasks){
//            taskService.removeUserFromTask(task.getTaskId(),userId,token);
//        }
        //deleting taskWorks submitted by users
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

    public boolean approveUser(UUID userId,UserRequest userRequest,String token){
        User user=getUserById(userId);
//        User approvingUser=getUserById(userRequest.getUserId());
        //check admin privilege. future check for admin jwt token
        if(!checkUserIsAdmin(token)){
            throw new CustomException("You do not have permission","Please contact the admin",HttpStatus.BAD_REQUEST);
        }
        if(userCreatedVerificationUsingId(userId)&&user.getUserStatus().equals(UserStatus.APPROVED)){
            throw new CustomException("User Already Approved","Proceed",HttpStatus.ACCEPTED);
        }
        if(userRequest.getRequestedRole().equals(Role.USER_ADMIN)){
            throw new CustomException("Cannot make user admin.","Please contact the admin",HttpStatus.BAD_REQUEST);
        }
        user.setUserRole(userRequest.getRequestedRole());
        user.setUserStatus(UserStatus.APPROVED);
        userRepo.save(user);
        return user.getUserStatus().equals(UserStatus.valueOf("APPROVED"));
    }

    public boolean checkUserIsAdmin(String token){
        return jwtService.extractUserRole(token).equals(Role.USER_ADMIN);
    }

    public  boolean userRoleVerification(UUID userId,Role role){
        User user=getUserById(userId);
        return user.getUserRole().equals(role);
    }

    public boolean userApprovalVerification(UUID userId){
        User user=getUserById(userId);
        return user.getUserStatus().equals(UserStatus.APPROVED);
    }

    public  String getToken(){
        String token="";
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            token= (String) authentication.getCredentials();
        }
        return token;
    }

    public boolean checkRole(String token,Role role){
        return jwtService.extractUserRole(token).equals(role);
    }

    public void claimedUserNameCheck(String token,String userName){
        if(getUserById(jwtService.extractUserId(token)).getUserName().equals(userName)){
            throw new CustomException("Username in the request payload and token don't match","Please verify the correct credentials and try again",HttpStatus.BAD_REQUEST);
        }
    }
    public List<Task> getAllTasksAssignedToUser(UUID userId){
        return userRepo.getAllTasksByUserId(userId);
    }
}
