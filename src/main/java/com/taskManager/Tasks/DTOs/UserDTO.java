package com.taskManager.Tasks.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private long userId;

    private String userName;

    private String occupation;

    private String status;

    private String dateJoined;

    private String approvalStatus="UnApproved";


}
