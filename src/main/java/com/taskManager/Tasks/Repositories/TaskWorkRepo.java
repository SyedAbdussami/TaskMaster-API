package com.taskManager.Tasks.Repositories;


import com.taskManager.Tasks.Models.TaskWork;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskWorkRepo extends CrudRepository<TaskWork,Long> {

    TaskWork getTaskWorkByTaskWorkId(long taskWorkId);

    @Query("select TaskWork from TaskWork taskWork where taskWork.task.taskId=:taskId")
    List<TaskWork> getAllByTaskId(long taskId);

    @Query("select TaskWork from TaskWork taskwork where taskwork.user.userId=:userId")
    TaskWork getTaskWorkByUserId(UUID userId);
}
