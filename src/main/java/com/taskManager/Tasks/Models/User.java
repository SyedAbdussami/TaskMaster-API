package com.taskManager.Tasks.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(name = "Name")
    private String userName;

    @Column(name="Occupation")
    private String userOccupation;

    @Column(name="Status")
    private String userStatus;

    @ManyToMany
    private List<Project> projects;

//    public User() {
//    }

    public User(long userId, String userName, String userOccupation, String userStatus, List<Project> projects) {
        this.userId = userId;
        this.userName = userName;
        this.userOccupation = userOccupation;
        this.userStatus = userStatus;
        this.projects = projects;
    }
}
