package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.holder;

import java.util.List;

public class ChatHolder {
    private int chatId;

    private String chatName;

    private List<Integer> users;

    public ChatHolder() {
    }

    public ChatHolder(int chatId) {
        this.chatId = chatId;
    }

    public ChatHolder(String chatName, List<Integer> usersIds) {
        this.chatName = chatName;
        this.users = usersIds;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }
}
