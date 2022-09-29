package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserDAOImplementation implements UserDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        User newUser = entityManager.merge(user);
        user.setId(newUser.getId());
    }

    @Override
    public User getUser(int id) {
        User user = entityManager.find(User.class, id);
        return user;
    }

//    @Override
//    public List<User> getAllUsers() {
//        Query query = entityManager.createQuery("from User");
//        List<User> allUsers = query.getResultList();
//
//        return allUsers;
//    }
//
//    @Override
//    public List<Chat> getUserChats(User user) {
//        return null;
//    }
}
