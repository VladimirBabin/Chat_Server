package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;

import java.util.List;

public interface UserService {
    public void save(User user);

    public User findById(int id);

    public List<User> findAll();

    void deleteById(int id);
}
