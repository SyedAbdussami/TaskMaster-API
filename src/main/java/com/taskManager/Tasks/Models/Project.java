package com.taskManager.Tasks.Models;

import com.taskManager.Tasks.Models.User;
import jakarta.persistence.*;
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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long projectId;

    @Column(name = "Project Name")
    private String projectName;

    @Column(name="Project Description")
    private String projectDescription;

    @Transient
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

    @Transient
    private List<UUID> userIds;


//    @Column(name="Created_Tasks")
//    @OneToMany
//    @JoinTable(name="Project_Tasks",
//    joinColumns = @JoinColumn(name="Project_ID"),
//    inverseJoinColumns = @JoinColumn(name = "Task_ID"))
//    private List<Task> tasks;

//    public Project() {
//    }

    public Project(long project_Id, String projectName, String projectDescription, String createdAt, List<User> users) {
        this.projectId = project_Id;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.users = users;
//        this.tasks=tasks;
    }
//
//    public long getProject_Id() {
//        return project_Id;
//    }
//
//    public String getProjectName() {
//        return projectName;
//    }
//
//    public String getProjectDescription() {
//        return projectDescription;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public List<User> getUsers() {
//        return users;
//    }
//
//    public void setProject_Id(long project_Id) {
//        this.project_Id = project_Id;
//    }
//
//    public void setProjectName(String projectName) {
//        this.projectName = projectName;
//    }
//
//    public void setProjectDescription(String projectDescription) {
//        this.projectDescription = projectDescription;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public void setUsers(List<User> users) {
//        this.users = users;
//    }
}
