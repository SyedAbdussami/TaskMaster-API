package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

     String addTask(Task task){
        taskRepo.save(task);
        return "Task added";
    }

}
