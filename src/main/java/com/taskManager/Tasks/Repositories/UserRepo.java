package com.taskManager.Tasks.Repositories;

import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

     Optional<User> findByUserName(String username);
     @Query(value = "select User from User User join User.tasks tasks where tasks.taskId=:taskId")
     List<User> findUsersByTaskId(@Param("taskId") long taskId);

     @Query(value = "select task from Task task JOIN task.users user where user.userId= :userId")
     List<Task> getAllTasksByUserId(@Param("userId") UUID userId);
}
