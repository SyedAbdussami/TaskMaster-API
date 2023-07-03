package com.taskManager.Tasks.DTOs;

import com.taskManager.Tasks.Enum.TaskStatus;
import com.taskManager.Tasks.Models.User;
import lombok.Getter;
import lombok.Setter;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class TaskDTO {

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private long taskId;

    private String taskName;

    private String taskDescription;

    private String createdAt;

    private List<UUID> assignedUsersIds;

    private TaskStatus taskStatus;

//    public Date setDateForTask(){
//        Date date=new Date();
//        taskCreatedDate=(Date)dateFormat.format(date);
//    }


}
