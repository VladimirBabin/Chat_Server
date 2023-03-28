package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.ChatRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    @Transactional
    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    @Transactional
    public List<Chat> findAllByUserId(int userId) {
        return chatRepository.findAllByUsersId(userId);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        chatRepository.deleteById(id);
    }

    @Override
    public Chat findById(int chatId) {
        Chat chat = null;
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            chat = chatOptional.get();
        } else {
            throw new NoSuchEntityException("There is no chat with id = " + chatId);
        }
        return chat;
    }

    @Override
    public Chat findByName(String name) {
        return chatRepository.findByName(name);
    }
}
