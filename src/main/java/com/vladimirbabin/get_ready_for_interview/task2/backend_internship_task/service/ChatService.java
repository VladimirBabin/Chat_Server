package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;

import java.util.List;

public interface ChatService {
    public void saveChat(Chat chat);

    public List<Chat> getAllChats();

    public List<Chat> getAllChatsFromUser(int userId);

    void deleteChat(int id);

    Chat getChatById(int chatId);
}
