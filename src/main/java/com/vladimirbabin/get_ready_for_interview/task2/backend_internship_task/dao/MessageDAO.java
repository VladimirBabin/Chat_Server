package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;

import java.util.List;

public interface MessageDAO {
    public void createMessage(Message message);

    List<Message> getMessagesFromChat(Chat chat);
}
