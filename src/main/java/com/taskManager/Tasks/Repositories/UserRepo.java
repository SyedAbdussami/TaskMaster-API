package com.taskManager.Tasks.Repositories;

import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepo extends CrudRepository<User,UUID> {

     User findUserByUserName(String userName);

     @Query(value = "select userId from User")
     Set<UUID> getAllUserIds();

     User findUsersByUserId(UUID userId);

     User findUserByFirstNameAndLastName(String firstName, String lastName);

     List<User> getUsersByUserRole(Role role);
}
