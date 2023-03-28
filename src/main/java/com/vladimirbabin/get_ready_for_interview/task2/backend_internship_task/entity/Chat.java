package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
@JsonIdentityInfo(
        scope = Chat.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "chat_name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_chat"
            , joinColumns = @JoinColumn(name = "chat_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties("chats")
    private List<User> users;

    @OneToMany(cascade = CascadeType.MERGE
            , mappedBy = "chat")
    @JsonIgnoreProperties("chat")
    private List<Message> messages;

    public Chat() {
    }

    public Chat(String name) {
        this.name = name;
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
        message.setChat(this);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
