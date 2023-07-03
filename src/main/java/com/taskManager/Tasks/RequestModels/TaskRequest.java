package com.taskManager.Tasks.RequestModels;

import com.taskManager.Tasks.Enum.TaskStatus;
import com.taskManager.Tasks.Models.Project;
import com.taskManager.Tasks.Models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TaskRequest {


    private long taskId;


    private String taskName;


    private String taskDescription;

    private Date createdAt;

    private long projectId;

    private TaskStatus taskStatus;


    private List<UUID> users;

    public TaskRequest() {
    }
}

