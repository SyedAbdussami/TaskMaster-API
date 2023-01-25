package com.taskManager.Tasks.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Entity
@Table(name="TASKS")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long taskId;

    @Column(name = "Task Name")
    private String taskName;

    @Column(name="Task Description")
    private String taskDescription;

    @Column(name="Date")
    private Date createdAt;

//    @Column(name = "Assigned Users")
//    private List<long> userIds;

    @ManyToOne
    @JoinColumn(name="projectId", nullable=false)
    private Project project;

    public Task() {
    }
}
