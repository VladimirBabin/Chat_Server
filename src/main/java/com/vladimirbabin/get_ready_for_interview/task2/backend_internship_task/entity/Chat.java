package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_chat"
            , joinColumns = @JoinColumn(name = "chat_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL
            , mappedBy = "messageChat")
    @JsonIgnore
    private List<Message> messages;

    public Chat() {
    }

    public Chat(String chatName) {
        this.chatName = chatName;
    }

    public void addUserToChat(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void addMessageToChat(Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
        message.setMessageChat(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
