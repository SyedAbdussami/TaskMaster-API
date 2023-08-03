package com.taskManager.Tasks.Models;


import com.taskManager.Tasks.Enum.TaskWorkStatus;
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

    @Column(name="FileName")
    private String fileName;

    @Column(name = "FileType")
    private String fileType;

    @Lob
    @Column(name = "File")
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name="taskId", nullable=false)
    private Task task;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private TaskWorkStatus taskWorkStatus;

}
