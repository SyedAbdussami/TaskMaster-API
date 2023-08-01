package com.taskManager.Tasks.RequestModels;

import com.taskManager.Tasks.Enum.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskWorkRequest {

    private String log;

    private String fileName;

    private String fileType;

    private String userName;
}
