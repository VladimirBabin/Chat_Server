package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.controller;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.*;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.ChatService;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.MessageService;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class RESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/users")
    public List<User> getAllUsers() {

        List<User> allUsers = userService.findAll();
        return allUsers;
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable int userId) {
        User user = userService.findById(userId);
        return user;
    }

    @PostMapping("/users")
    public int addNewUser(@RequestBody User user) {
            if (user == null || user.getUserName() == null || user.getUserName().isBlank()) {
                throw new NoTextMessageEntered("No user name was entered");
            }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        userService.save(user);
        return user.getId();
    }

    @PutMapping("/users")
    public int updateUser(@RequestBody User user) {
        if (user == null || user.getUserName() == null || user.getUserName().isBlank()) {
            throw new NoTextMessageEntered("No user name was entered");
        }
        if (user.getId() == 0) {
            throw new NoTextMessageEntered("No user id was entered");
        }
        userService.save(user);
        return user.getId();
    }

    @DeleteMapping("users/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteById(userId);
        return "User with ID = " + userId +" was deleted";
    }


    @GetMapping("/chats")
    public List<Chat> getAllChats() {

        List<Chat> allChats = chatService.findAll();
        return allChats;
    }

    @GetMapping("/chats/{chatId}")
    public Chat getChatById(@PathVariable int chatId) {
        Chat chat = chatService.findById(chatId);
        return chat;
    }


    @PostMapping("/chats")
    public int saveNewChatBetweenUsers(@RequestBody Chat chat) {

        if (chat == null) {
            throw new NoSuchEntityException("Chat doesn't exist");
        }
        if (chat.getCreatedAt() == null) {
            chat.setCreatedAt(LocalDateTime.now());
        }

        chatService.save(chat);

        return chat.getId();
    }

    @PutMapping("/chats")
    public int updateChatBetweenUsers(@RequestBody Chat chat) {

        if (chat == null) {
            throw new NoSuchEntityException("Chat doesn't exist");
        }

        if (chat.getId() == 0) {
            throw new NoTextMessageEntered("No chat id was entered");
        }
        chatService.save(chat);

        return chat.getId();
    }

    @PostMapping("/chats/get_by_user")
    public List<Chat> getAllChatsOfUser(@RequestBody User user) {

        List<Chat> listOfChats = chatService.findAllByUserId(user.getId());
        if (listOfChats == null || listOfChats.isEmpty()) {
            throw new NoSuchEntityException("There are no chats for this user");
        }
        return listOfChats;
    }

    @DeleteMapping("chats/{chatId}")
    public String deleteChat(@PathVariable int chatId) {
        chatService.deleteById(chatId);
        return "Chat with ID = " + chatId +" was deleted";
    }

    @GetMapping("messages/{messageId}")
    public Message getMessage(@PathVariable int messageId) {
        Message message = messageService.findById(messageId);
        if (message == null) {
            throw new NoSuchEntityException("There is no message with id = " + messageId);
        }
        return message;
    }

    @PostMapping("/messages")
    public int addNewMessageFromUserToChat(@RequestBody Message message) {
        Chat chat = chatService.findById(message.getChat().getId());
        if (chat == null) {
            throw new NoSuchEntityException("Chat doesn't exist");
        }
        if (message.getText() == null || message.getText().isBlank()) {
            throw new NoTextMessageEntered("No text message was entered");
        }
        User user = userService.findById(message.getAuthor().getId());
        if (user == null) {
            throw new NoSuchEntityException("There is no user with id = " + message.getAuthor().getId());
        }
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(LocalDateTime.now());
        }
        messageService.save(message);
        return message.getId();
    }

    @PutMapping("/messages")
    public int updateMessageFromUserToChat(@RequestBody Message message) {
        if (message.getId() == 0) {
            throw new NoTextMessageEntered("No message id was entered");
        }
        if (message.getText() == null) {
            throw new NoTextMessageEntered("No message text was entered");
        }
        messageService.save(message);
        return message.getId();
    }

    @PostMapping("messages/get_by_chat")
    public List<Message> getAllMessagesOfChat(@RequestBody Chat chat) {
        if (chat == null) {
            throw new NoSuchEntityException("Chat doesn't exist");
        }
        List<Message> listOfMessages = messageService.findAllByChat(chat);
        if (listOfMessages == null || listOfMessages.isEmpty()) {
            throw new NoSuchEntityException("There are no messages in this chat");
        }

        return listOfMessages;
    }

    @DeleteMapping("messages/{messageId}")
    public String deleteMessage(@PathVariable int messageId) {
        messageService.deleteById(messageId);
        return "Message with ID = " + messageId +" was deleted";
    }



}
