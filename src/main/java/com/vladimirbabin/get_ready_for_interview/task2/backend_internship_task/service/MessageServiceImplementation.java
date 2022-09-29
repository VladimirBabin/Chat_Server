package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.MessageDAO;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MessageServiceImplementation implements MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Override
    @Transactional
    public void createMessage(Message message) {
        messageDAO.createMessage(message);
    }

    @Override
    public List<Message> getMessagesFromChat(Chat chat) {
        return messageDAO.getMessagesFromChat(chat);
    }
}
