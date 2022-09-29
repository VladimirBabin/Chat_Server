package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.holder;

public class MessageHolder {
    private int chatId;

    private int authorId;

    private String text;

    public MessageHolder() {
    }

    public MessageHolder(int chatId, int authorId, String text) {
        this.chatId = chatId;
        this.authorId = authorId;
        this.text = text;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
