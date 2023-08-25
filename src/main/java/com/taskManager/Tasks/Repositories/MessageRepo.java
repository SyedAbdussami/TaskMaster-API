package com.taskManager.Tasks.Repositories;


import com.taskManager.Tasks.Models.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepo extends CrudRepository<Message,Long> {

    @Query(value = "SELECT Message from Message message where message.user.userId=:userId")
    List<Message> getMessagesByUserId(UUID userId);
}
