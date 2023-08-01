package com.taskManager.Tasks.Models;

import com.taskManager.Tasks.Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PROJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long projectId;

    @Column(name = "Project Name")
    private String projectName;

    @Column(name="Project Description")
    private String projectDescription;

    @Column(name="Date")
    private String createdAt;

//    @Column(name = "Assigned Users")
//    @OneToMany(mappedBy = "project")
//    private List<long> userIds;

//    @Column(name = "Assigned Users")
//    @OneToMany(mappedBy = "user")
//    private List<User> users=new ArrayList<>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "Project_Users",
    joinColumns = @JoinColumn(name="projectId"),
    inverseJoinColumns = @JoinColumn(name="userId"))
    private List<User> users;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @Transient
    private List<UUID> userIds;

    @Transient
    private List<Long> taskIds;

}
