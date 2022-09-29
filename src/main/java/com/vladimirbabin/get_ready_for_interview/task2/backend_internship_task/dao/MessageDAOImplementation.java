package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MessageDAOImplementation implements MessageDAO {

    @Autowired
    private EntityManager entityManager;


    @Override
    public void createMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        Message newMessage = entityManager.merge(message);
        message.setId(newMessage.getId());
    }

    @Override
    public List<Message> getMessagesFromChat(Chat chat) {
        Query query = entityManager.createQuery("from Message message where message.messageChat = :thatChat order by message.createdAt desc");
        query.setParameter("thatChat", chat);
        List<Message> messages = query.getResultList();
        return messages;
    }
}
