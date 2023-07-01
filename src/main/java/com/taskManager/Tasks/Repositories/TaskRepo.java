package com.taskManager.Tasks.Repositories;

import com.taskManager.Tasks.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends CrudRepository<Task,Long> {

    Task getTaskByTaskId(long taskId);
}
