package com.taskManager.Tasks.Seeders;


import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Enum.UserStatus;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UserSeeder implements CommandLineRunner {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    @Override
    public void run(String... args){
        if(userRepo.getUsersByUserRole(Role.USER_ADMIN).isEmpty()){
            User admin=new User();
            admin.setUserName("Admin");
            admin.setFirstName("Syed");
            admin.setLastName("Abdussami");
            admin.setUserRole(Role.USER_ADMIN);
            admin.setUserOccupation("Software Guy");
            admin.setDateJoined(dtf.format(LocalDateTime.now()));
            admin.setUserStatus(UserStatus.APPROVED);
            admin.setPassword(passwordEncoder.encode("coolPassword"));
            userRepo.save(admin);
        }
        if(userRepo.getUsersByUserRole(Role.USER_MANAGER).isEmpty()){
            User admin=new User();
            admin.setUserName("Manager");
            admin.setFirstName("Syed");
            admin.setLastName("Abd");
            admin.setUserRole(Role.USER_MANAGER);
            admin.setUserOccupation("Manager guy 1");
            admin.setDateJoined(dtf.format(LocalDateTime.now()));
            admin.setUserStatus(UserStatus.APPROVED);
            admin.setPassword(passwordEncoder.encode("coolPassword123"));
            userRepo.save(admin);
        }

    }
}
