package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;

import java.util.List;

public interface UserDAO {
    public void saveUser(User user);

    public User getUser(int id);

//    public List<User> getAllUsers();
//
//    public List<Chat> getUserChats(User user);
}
