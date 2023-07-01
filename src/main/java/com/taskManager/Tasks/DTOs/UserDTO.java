package com.taskManager.Tasks.DTOs;

import com.taskManager.Tasks.Models.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private UUID userId;

    private String userName;

    private String occupation;

    private String status;

    private String dateJoined;

    private String approvalStatus="UnApproved";

    private String firstName;

    private String lastName;

    private List<Long> projectIds;


}
