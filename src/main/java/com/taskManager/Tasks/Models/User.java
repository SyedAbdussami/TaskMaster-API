package com.taskManager.Tasks.Models;

import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Enum.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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

    @Column(name = "Password")
    private String password;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name="LastName")
    private String lastName;

    @Column(name="Occupation")
    private String userOccupation;

    @Enumerated(EnumType.STRING)
    @Column(name="User_Status")
    private UserStatus userStatus;

    @Column(name="DateJoined")
    private String dateJoined;

    @ManyToMany
    private List<Project> projects;

    @ManyToMany
    private List<Task> tasks;

//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "userId",referencedColumnName = "userId"),inverseJoinColumns = @JoinColumn(name = "roleId",referencedColumnName = "roleId"))
//    Set<Role> role=new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "User_Role")
    private Role userRole;

//    public User() {
//    }

    public User(UUID userId, String userName, String userOccupation, List<Project> projects) {
        this.userId = userId;
        this.userName = userName;
        this.userOccupation = userOccupation;
        this.projects = projects;
    }
}
