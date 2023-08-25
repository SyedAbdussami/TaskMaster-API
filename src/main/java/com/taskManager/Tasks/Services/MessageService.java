package com.taskManager.Tasks.Services;

import com.taskManager.Tasks.DTOs.MessageDTO;
import com.taskManager.Tasks.Enum.Role;
import com.taskManager.Tasks.Exception.CustomException;
import com.taskManager.Tasks.Models.Message;
import com.taskManager.Tasks.Repositories.MessageRepo;
import com.taskManager.Tasks.Security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    JwtService jwtService;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    ModelMapper mapper = new ModelMapper();

    public  void createMessage(MessageDTO obj) {
        Message message = mapper.map(obj, Message.class);
        if (message.getContent().isEmpty()) {
            message.setContent("No details specified");
        }
        message.setDate(dtf.format(LocalDateTime.now()));
        messageRepo.save(message);
    }

    public List<MessageDTO> fetchAllMessages(String token){
        MessagePermissionCheck(token,"View system messages");
        UUID userId=jwtService.extractUserId(token);
        return messageRepo.getMessagesByUserId(userId).stream().map(message -> mapper.map(message,MessageDTO.class)).toList();
    }

    public void MessagePermissionCheck(String token, String actionType) {
        if (!authenticationService.permissionCheck(token, Role.USER_ADMIN) || !authenticationService.permissionCheck(token, Role.USER_MANAGER)) {
            throw new CustomException("You do not have the permission to " + actionType + " this task", "Please send a separate request to Admin or your assigned Manager", HttpStatus.UNAUTHORIZED);
        }
    }

}
