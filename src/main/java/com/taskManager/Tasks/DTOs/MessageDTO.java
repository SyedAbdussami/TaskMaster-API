package com.taskManager.Tasks.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private long messageId;

    private String content;

    private long projectId;

    private long taskId;

    private long taskWorkId;

    private String userName;
}
