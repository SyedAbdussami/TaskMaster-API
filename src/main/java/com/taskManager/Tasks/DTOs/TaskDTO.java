package com.taskManager.Tasks.DTOs;

import com.taskManager.Tasks.Models.User;
import lombok.Getter;
import lombok.Setter;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class TaskDTO {

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private long taskId;

    private String taskName;

    private String taskDescription;

    private Date createdAt;

    private List<User> assignedUsersTask;

//    public Date setDateForTask(){
//        Date date=new Date();
//        taskCreatedDate=(Date)dateFormat.format(date);
//    }


}
