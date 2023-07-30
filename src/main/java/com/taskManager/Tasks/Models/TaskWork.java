package com.taskManager.Tasks.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskWork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long taskWorkId;
    @Column(name = "Log")
    private String log;
    @Column(name = "Date")
    private Date date;

//    @Column(name = "Files")
//    private MultipartFile file;

    @ManyToOne
    @JoinColumn(name="taskId", nullable=false)
    private Task task;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;
}
