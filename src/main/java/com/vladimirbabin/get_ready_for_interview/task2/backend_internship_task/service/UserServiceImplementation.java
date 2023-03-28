package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.UserRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(int id) {
        User user = null;
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            throw new NoSuchEntityException("There is no user with id = " + id);
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        try {
            userRepository.deleteById(id);
        } catch(Exception e) {
            throw new NoSuchEntityException("There is no user with id = " + id);
        }
    }
}
