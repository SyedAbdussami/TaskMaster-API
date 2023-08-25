package com.taskManager.Tasks.Models;

import com.taskManager.Tasks.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "taskId")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "taskWorkId")
    private TaskWork taskWork;

    @Column(name = "Date")
    private String date;

    @Column(name = "message")
    private String content;

    @Enumerated(EnumType.STRING)
    private Role Viewer;

}
