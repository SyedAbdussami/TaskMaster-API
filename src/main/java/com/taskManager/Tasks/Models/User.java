package com.taskManager.Tasks.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

//import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name="LastName")
    private String lastName;

    @Column(name="Occupation")
    private String userOccupation;

    @Column(name="Status")
    private String userStatus;

    @Column(name="DateJoined")
    private String dateJoined;

    @ManyToMany
    private List<Project> projects;

    @ManyToMany
    private List<Task> tasks;

//    public User() {
//    }

    public User(UUID userId, String userName, String userOccupation, String userStatus, List<Project> projects) {
        this.userId = userId;
        this.userName = userName;
        this.userOccupation = userOccupation;
        this.userStatus = userStatus;
        this.projects = projects;
    }
}
