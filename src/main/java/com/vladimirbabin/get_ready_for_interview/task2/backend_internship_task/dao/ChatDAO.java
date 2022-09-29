package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;

import java.util.List;

public interface ChatDAO {
    public List<Chat> getAllChats();

    public void saveChat(Chat chat);

    public List<Chat> getAllChatsFromUser(int userId);

    void deleteChat(int id);

    Chat getChatById(int chatId);
}
