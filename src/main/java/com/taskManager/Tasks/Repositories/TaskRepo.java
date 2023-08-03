package com.taskManager.Tasks.Repositories;

import com.taskManager.Tasks.Enum.TaskPriority;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepo extends CrudRepository<Task,Long> {

    Task getTaskByTaskId(long taskId);

    List<Task> getTasksByTaskPriority(TaskPriority taskPriority);

}
