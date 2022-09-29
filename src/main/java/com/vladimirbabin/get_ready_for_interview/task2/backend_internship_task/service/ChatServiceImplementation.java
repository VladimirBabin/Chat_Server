package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.ChatDAO;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.UserDAO;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChatServiceImplementation implements ChatService {

    @Autowired
    private ChatDAO chatDAO;

    @Override
    @Transactional
    public void saveChat(Chat chat) {
        chatDAO.saveChat(chat);
    }

    @Override
    public List<Chat> getAllChats() {
        return chatDAO.getAllChats();
    }

    @Override
    @Transactional
    public List<Chat> getAllChatsFromUser(int userId) {
        return chatDAO.getAllChatsFromUser(userId);
    }

    @Override
    @Transactional
    public void deleteChat(int id) {
        chatDAO.deleteChat(id);
    }

    @Override
    public Chat getChatById(int chatId) {
        return chatDAO.getChatById(chatId);
    }
}
