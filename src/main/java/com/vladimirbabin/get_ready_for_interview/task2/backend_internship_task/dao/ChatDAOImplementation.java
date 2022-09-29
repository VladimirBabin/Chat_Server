package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ChatDAOImplementation implements ChatDAO{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Chat> getAllChats() {
        Query query = entityManager.createQuery("from Chat");
        List<Chat> allChats = query.getResultList();
        return allChats;
    }

    @Override
    public void saveChat(Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        Chat newChat = entityManager.merge(chat);
        chat.setId(newChat.getId());
    }

    @Override
    public List<Chat> getAllChatsFromUser(int userId) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new NoSuchEntityException("There is no such user");
        }
        Query query = entityManager.createQuery("select user.chats from User user where user = :theUser");
        query.setParameter("theUser", user);
        List<Chat> allChats = query.getResultList();
        return allChats;
    }

    @Override
    public void deleteChat(int id) {
        Query query = entityManager.createQuery("delete from Chat where id = :chatId");
        query.setParameter("chatId", id);
        query.executeUpdate();
    }

    @Override
    public Chat getChatById(int chatId) {
        Chat chat = entityManager.find(Chat.class, chatId);
        return chat;
    }
}
