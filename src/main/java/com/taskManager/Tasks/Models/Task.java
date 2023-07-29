package com.taskManager.Tasks.Models;

import com.taskManager.Tasks.Enum.TaskPriority;
import com.taskManager.Tasks.Enum.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(name="Status")
    private TaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="Priority")
    private TaskPriority taskPriority;


//    @Column(name = "Assigned Users")
//    private List<long> userIds;

    @ManyToOne
    @JoinColumn(name="projectId", nullable=false)
    private Project project;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "Tasks_Users",
            joinColumns = @JoinColumn(name="taskId"),
            inverseJoinColumns = @JoinColumn(name="userId"))
    private List<User> users;

    public Task() {
    }
}
