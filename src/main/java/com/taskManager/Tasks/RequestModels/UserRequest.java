package com.taskManager.Tasks.RequestModels;

import com.taskManager.Tasks.Models.Project;
import com.taskManager.Tasks.Models.Task;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserRequest {
    private UUID userId;

    private String userName;

    private String firstName;


    private String lastName;


    private String userOccupation;


    private String userStatus;


    private List<Long> projectIds;


    private List<Long> tasksIds;
}
