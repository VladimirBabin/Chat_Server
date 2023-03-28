package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;

import java.util.List;

public interface ChatService {
    public void save(Chat chat);

    public List<Chat> findAll();

    public List<Chat> findAllByUserId(int userId);

    void deleteById(int id);

    Chat findById(int chatId);

    Chat findByName(String name);
}
