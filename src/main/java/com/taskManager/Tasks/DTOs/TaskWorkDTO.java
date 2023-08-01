package com.taskManager.Tasks.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskWorkDTO {
    private  long taskWorkId;

    private String log;

    private String fileName;

    private String fileType;

    private String fileData;

    private String userName;
}
