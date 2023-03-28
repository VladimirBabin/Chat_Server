package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.MessageRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImplementation implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    @Transactional
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> findAllByChat(Chat chat) {
        return messageRepository.findAllByChat(chat);
    }

    @Override
    public void deleteById(int messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public Message findById(int messageId) {

        Message message = null;
        Optional<Message> optional = messageRepository.findById(messageId);
        if (optional.isPresent()) {
            message = optional.get();
        }

        return message;
    }
}
