package com.taskManager.Tasks.RequestModels;

import com.taskManager.Tasks.Enum.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskWorkRequest {

    private  long taskId;


    private String proof;

}
