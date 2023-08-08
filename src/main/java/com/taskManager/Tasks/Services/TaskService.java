package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.DTOs.TaskDTO;
import com.taskManager.Tasks.DTOs.TaskWorkDTO;
import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Enum.TaskPriority;
import com.taskManager.Tasks.Enum.TaskStatus;
import com.taskManager.Tasks.Enum.TaskWorkStatus;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Task;
import com.taskManager.Tasks.Models.TaskWork;
import com.taskManager.Tasks.Models.User;
import com.taskManager.Tasks.Repositories.ProjectRepo;
import com.taskManager.Tasks.Repositories.TaskRepo;
import com.taskManager.Tasks.Repositories.TaskWorkRepo;
import com.taskManager.Tasks.Repositories.UserRepo;
import com.taskManager.Tasks.RequestModels.TaskRequest;
import com.taskManager.Tasks.RequestModels.TaskWorkRequest;
import com.taskManager.Tasks.RequestModels.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    TaskWorkRepo taskWorkRepo;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    AuthenticationService authenticationService;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    ModelMapper mapper = new ModelMapper();

    public TaskDTO addTask(long projectId, TaskRequest taskRequest, String token) {
//         if( !authenticationService.permissionCheck(token,Role.USER_ADMIN)||!authenticationService.permissionCheck(token,Role.USER_MANAGER)){
//             throw new CustomException("You do not have the permission to create this task","Please send a separate request to Admin or your assigned Manager",HttpStatus.UNAUTHORIZED);
//         }
        taskPermissionCheck(token, "create");
        Task task = mapper.map(taskRequest, Task.class);
        if (!projectService.verifyProjectExistsUsingId(projectId) && !userService.verifyUsersCreatedUsingId(taskRequest.getUsers())) {
            throw new CustomException("Please verify the project name or the list of user Ids", "", HttpStatus.BAD_REQUEST);
        }
        if (taskRequest.getTaskPriority() != null) {
            task.setTaskPriority(TaskPriority.UNDEFINED);
        }
        taskRepo.save(task);
        TaskDTO taskDTO = mapper.map(task, TaskDTO.class);
        taskDTO.setCreatedAt(dtf.format(LocalDateTime.now()));
        taskDTO.setAssignedUsersIds(taskRequest.getUsers());
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = (List<Task>) taskRepo.findAll();
        List<TaskDTO> taskDTOS = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOS.add(mapper.map(task, TaskDTO.class));
        }
        return taskDTOS;
    }

    public Task getTaskById(long taskId) {
        return taskRepo.getTaskByTaskId(taskId);
    }

    public TaskDTO assignUserTask(long projectId, long taskId, UserRequest userRequest, String token) {
        verifyTaskCreated(taskId);
        taskPermissionCheck(token, "assign users");
        projectService.checkUserBelongsToProject(userRequest.getUserId(), projectId);
        if (!projectService.verifyProjectExistsUsingId(projectId) && !userService.userCreatedVerificationUsingId(userRequest.getUserId())) {
            throw new CustomException("Please check Project Id", "Contact the admin", HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepo.getTaskByTaskId(taskId);
        List<User> users = task.getUsers();
        User newUser = userService.getUserById(userRequest.getUserId());
        users.add(newUser);
        task.setUsers(users);
        taskRepo.save(task);
        TaskDTO updatedTaskDTO = mapper.map(task, TaskDTO.class);
        updatedTaskDTO.setAssignedUsersIds(fetchUserIdsByTask(taskId));
        return updatedTaskDTO;
    }

    public void verifyTaskCreated(long taskId) {
        if (taskRepo.findById(taskId).isEmpty()) {
            throw new CustomException("Task does not exist", "Try again later", HttpStatus.BAD_REQUEST);
        }
    }

    public TaskDTO updateTaskDetails(long taskId, long projectId, TaskRequest taskRequest, String token) {
//        if( !authenticationService.permissionCheck(token,Role.USER_ADMIN)||!authenticationService.permissionCheck(token,Role.USER_MANAGER)){
//            throw new CustomException("You do not have the permission to update this task","Please send a separate request to Admin or your assigned Manager",HttpStatus.UNAUTHORIZED);
//        }
        taskPermissionCheck(token, "update");
        if (verifyTaskBelongsToProject(taskId, projectId)) {
            throw new CustomException("Task is not assigned to the given project", "Contact your Manager", HttpStatus.BAD_REQUEST);
        }
        Task oldTask = taskRepo.getTaskByTaskId(taskId);
        Task newTask = mapper.map(taskRequest, Task.class);
        if (Objects.equals(newTask.getTaskName(), oldTask.getTaskName())) {
            newTask.setTaskId(oldTask.getTaskId());
        }
        taskRepo.save(newTask);
        return mapper.map(newTask, TaskDTO.class);
    }

    public boolean deleteTask(long taskId, long projectId, String token) {
//        if( !authenticationService.permissionCheck(token,Role.USER_ADMIN)||!authenticationService.permissionCheck(token,Role.USER_MANAGER)){
//            throw new CustomException("You do not have the permission to delete this task","Please send a separate request to Admin or your assigned Manager",HttpStatus.UNAUTHORIZED);
//        }
        taskPermissionCheck(token, "delete");
        if (verifyTaskBelongsToProject(taskId, projectId)) {
            throw new CustomException("Task is not assigned to the given project", "Contact your Manager", HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepo.getTaskByTaskId(taskId);
        taskRepo.delete(task);
        return taskRepo.getTaskByTaskId(taskId) == null;
    }

    public TaskDTO updateTaskStatus(long taskId, TaskRequest taskRequest, String token) {
//        if( !authenticationService.permissionCheck(token,Role.USER_ADMIN)||!authenticationService.permissionCheck(token,Role.USER_MANAGER)){
//            throw new CustomException("You do not have the permission to delete this task","Please send a separate request to Admin or your assigned Manager",HttpStatus.UNAUTHORIZED);
//        }
        taskPermissionCheck(token, "update status of");
        verifyTaskCreated(taskId);
        Task task = taskRepo.getTaskByTaskId(taskId);
        TaskStatus oldStatus = task.getTaskStatus();
        TaskStatus newStatus = taskRequest.getTaskStatus();
        if (oldStatus.equals(newStatus)) {
            throw new CustomException("Status can not be changed", "Requested status change is same as current task status", HttpStatus.BAD_REQUEST);
        }
        task.setTaskStatus(newStatus);
        taskRepo.save(task);
        return mapper.map(task, TaskDTO.class);
    }

    public TaskDTO removeUsersFromTask(long taskId, List<UUID> userIds, String token) {
//        if( !authenticationService.permissionCheck(token,Role.USER_ADMIN)||!authenticationService.permissionCheck(token,Role.USER_MANAGER)){
//            throw new CustomException("You do not have the permission to delete this task","Please send a separate request to Admin or your assigned Manager",HttpStatus.UNAUTHORIZED);
//        }
        taskPermissionCheck(token, "remove users from");
        verifyTaskCreated(taskId);
        if (!userService.verifyUsersCreatedUsingId(userIds)) {
            throw new CustomException("Users Id's are not correct", "Please try again correctly or contact the admin", HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepo.getTaskByTaskId(taskId);
        List<UUID> actualUsers = task.getUsers().stream().map(User::getUserId).toList();
//         List<UUID> updatedUserList= task.getUsers().stream().filter(user. -> !userIds.contains(user.getUserId())).toList();
        List<UUID> updatedUserList = actualUsers.stream().filter(userIds::contains).toList();
        task.setUsers(userService.getUsersByIds(updatedUserList));
        taskRepo.save(task);
        return mapper.map(task, TaskDTO.class);
    }

    public TaskDTO removeUserFromTask(long taskId, UUID userId, String token) {
        taskPermissionCheck(token, "remove users from");
        verifyTaskCreated(taskId);
        if (!userService.userCreatedVerificationUsingId(userId)) {
            throw new CustomException("Users Id is not correct", "Please try again correctly or contact the admin", HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepo.getTaskByTaskId(taskId);
       List<User> users=task.getUsers();
       users.remove(userRepo.findUsersByUserId(userId));
        task.setUsers(users);
        taskRepo.save(task);
        return mapper.map(task, TaskDTO.class);
    }

    public TaskDTO taskWorkUpdate(long taskId, TaskWorkRequest taskWorkRequest, UUID userId) {
        verifyTaskCreated(taskId);
        if (userService.userCreatedVerificationUsingId(userId)) {
            throw new CustomException("Given task does not exist", "Please try again with a valid task ID", HttpStatus.NOT_FOUND);
        }

        //Admin token check
        Task task = getTaskById(taskId);
        long projectId = task.getProject().getProjectId();
        //Manager approval
        return mapper.map(task, TaskDTO.class);

    }

    public List<UUID> fetchUserIdsByTask(long taskIds) {
        Task task = getTaskById(taskIds);
        return task.getUsers().stream().map(User::getUserId).toList();
    }

    public long getProjectIdFromTaskId(long taskId) {
        verifyTaskCreated(taskId);
        Task task = getTaskById(taskId);
        return task.getProject().getProjectId();
    }

    public boolean checkTaskAccessPrivilege(UUID userId) {
        User user = userService.getUserById(userId);
        return !user.getUserRole().equals(Role.USER_ADMIN) || !user.getUserRole().equals(Role.USER_MANAGER);
    }

    public boolean verifyTaskBelongsToProject(long taskId, long projectId) {
        Task task = getTaskById(taskId);
        return task.getProject().getProjectId() == projectId;
    }

    public TaskDTO changeTaskPriority(long taskId, String taskPriority, String token) {
        taskPermissionCheck(token, "Change priority of");
        verifyTaskCreated(taskId);
        Task task = getTaskById(taskId);
        if (task.getTaskPriority() == TaskPriority.valueOf(taskPriority)) {
            throw new CustomException("Task priority cannot be changed", "Same as requested", HttpStatus.BAD_REQUEST);
        }
        task.setTaskPriority(TaskPriority.valueOf(taskPriority));
        return mapper.map(task, TaskDTO.class);
    }

    public void taskPermissionCheck(String token, String actionType) {
        if (!authenticationService.permissionCheck(token, Role.USER_ADMIN) || !authenticationService.permissionCheck(token, Role.USER_MANAGER)) {
            throw new CustomException("You do not have the permission to " + actionType + " this task", "Please send a separate request to Admin or your assigned Manager", HttpStatus.UNAUTHORIZED);
        }
    }

    public List<TaskDTO> getTasksWithPriority(String taskPriority,String token) {
        taskPermissionCheck(token,"");
        List<TaskDTO> tasks = taskRepo.getTasksByTaskPriority(TaskPriority.valueOf(taskPriority)).stream().map(task -> mapper.map(task, TaskDTO.class)).toList();
        if (tasks.size() == 0) {
            throw new CustomException("No Tasks with priority " + taskPriority + " do not exist", "Please specify the correct priority", HttpStatus.BAD_REQUEST);
        }
        return tasks;
    }

    public List<User> getUsersAssignedToTask(long taskId, String token) {
        verifyTaskCreated(taskId);
        taskPermissionCheck(token, "get users assigned to");
        List<User> users = userRepo.findUsersByTaskId(taskId);
        if (users.size() == 0) {
            throw new CustomException("No users are assigned to this task", "", HttpStatus.NOT_FOUND);
        }
        return users;
    }

    public List<TaskWorkDTO> getAllWorkDoneOnTask(long taskId, String token) {
        taskPermissionCheck(token, "");
        verifyTaskCreated(taskId);
        return taskWorkRepo.getAllByTaskId(taskId).stream().map(taskWork -> mapper.map(taskWork, TaskWorkDTO.class)).toList();
    }

    public TaskWorkDTO createWorkOnTask(long taskId, TaskWorkRequest taskWorkRequest, MultipartFile file,String token) {
        userService.claimedUserNameCheck(token,taskWorkRequest.getUserName());
        verifyTaskCreated(taskId);
        TaskWork taskWork = mapper.map(taskWorkRequest, TaskWork.class);
        taskWork.setTaskWorkStatus(TaskWorkStatus.SUBMITTED);
        //call to message queue for managers
        try {
            taskWork.setFileData(file.getBytes());
            taskWorkRepo.save(taskWork);
            return mapper.map(taskWorkRepo.getTaskWorkByUserId(userRepo.findUserByUserName(taskWorkRequest.getUserName()).getUserId()), TaskWorkDTO.class);
        } catch (IOException ex) {
            throw new CustomException(ex.getMessage(), "Please tryAgain", HttpStatus.BAD_REQUEST);
        }
    }

    public TaskWorkDTO getWorkDoneOnTask(long taskId, long taskWorkId, String token) {
        verifyTaskCreated(taskId);
        taskPermissionCheck(token, "");
        return mapper.map(taskWorkRepo.findById(taskWorkId), TaskWorkDTO.class);
    }

    public TaskWorkDTO approveTaskWork(long taskId,long taskWorkId,String token){
        verifyTaskCreated(taskId);
        taskPermissionCheck(token, "approve a submitted task work for");
        TaskWork taskWork=taskWorkRepo.getTaskWorkByTaskWorkId(taskWorkId);
        taskWork.setTaskWorkStatus(TaskWorkStatus.APPROVED);
        if(getAllWorkDoneOnTask(taskId,token).isEmpty()){
            Task task=getTaskById(taskId);
            task.setTaskStatus(TaskStatus.PENDING_REVIEW);
            taskRepo.save(task);
        }
        return mapper.map(taskWork,TaskWorkDTO.class);
    }
    public TaskWorkDTO rejectTaskWork(long taskId,long taskWorkId,String token){
        verifyTaskCreated(taskId);
        taskPermissionCheck(token, "approve a submitted task work for");
        TaskWork taskWork=taskWorkRepo.getTaskWorkByTaskWorkId(taskWorkId);
        taskWork.setTaskWorkStatus(TaskWorkStatus.REJECTED);
        return mapper.map(taskWork,TaskWorkDTO.class);
    }

    public List<TaskWorkDTO> deleteTaskWork(long taskId,long taskWorkId,String token){
        verifyTaskCreated(taskId);
        taskPermissionCheck(token, "approve a submitted task work for");
        TaskWork taskWork=taskWorkRepo.getTaskWorkByTaskWorkId(taskWorkId);
        List<TaskWorkDTO> taskWorks= getAllWorkDoneOnTask(taskId, token);
        taskWorks.remove(mapper.map(taskWork,TaskWorkDTO.class));
        taskWorkRepo.delete(taskWork);
        return taskWorks;
    }

}
