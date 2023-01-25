package com.taskManager.Tasks.DTOs;

import com.taskManager.Tasks.Models.User;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class ProjectDTO {
    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private long projectId;

    private String projectName;

    private String projectDescription;

    private Date projectCreatedDate;

    private List<User> assignedUsersProject;
}
