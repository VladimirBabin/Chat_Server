package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.controller;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.*;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.holder.ChatHolder;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.holder.MessageHolder;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.holder.UserHolder;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.ChatService;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.MessageService;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

//    @GetMapping("/users")
//    public List<User> getAllUsers() {
//
//        List<User> allUsers = userService.getAllUsers();
//        return allUsers;
//    }
//
//    @GetMapping("/chats")
//    public List<Chat> getAllChats() {
//
//        List<Chat> allChats = chatService.getAllChats();
//        return allChats;
//    }


    @PostMapping("/users/add")
    public int addNewUser(@RequestBody User user) {
        if (user == null || user.getUserName() == null || user.getUserName().isBlank()) {
            throw new RuntimeException("No user name was entered");
        }
        userService.saveUser(user);
        return user.getId();
    }


    @PostMapping("/chats/add")
    public int saveNewChatBetweenUsers(@RequestBody ChatHolder chatHolder) {
        Chat chat = new Chat(chatHolder.getChatName());
        if (chat == null) {
            throw new NoSuchEntityException("Chat number" + chatHolder.getChatId() + "doesn't exist");
        }
        for (int userId : chatHolder.getUsers()) {
            chat.addUserToChat(userService.getUser(userId));
        }
        chatService.saveChat(chat);

        return chat.getId();
    }

    @PostMapping("/messages/add")
    public int addNewMessageFromUserToChat(@RequestBody MessageHolder messageHolder) {
        Chat chat = chatService.getChatById(messageHolder.getChatId());
        if (chat == null) {
            throw new NoSuchEntityException("Chat number" + messageHolder.getChatId() + "doesn't exist");
        }
        if (messageHolder.getText() == null || messageHolder.getText().isBlank()) {
            throw new NoTextMessageEntered("No text message was entered");
        }
        User user = userService.getUser(messageHolder.getAuthorId());
        Message message = new Message(chat, user, messageHolder.getText());
        messageService.createMessage(message);
        return message.getId();
    }

    @PostMapping("/chats/get")
    public List<Chat> getAllChatsOfUser(@RequestBody UserHolder userHolder) {
        if (userHolder.getUser() == 0) {
            throw new RuntimeException("Bad request");
        }
        List<Chat> listOfChats = chatService.getAllChatsFromUser(userHolder.getUser());
        if (listOfChats == null || listOfChats.isEmpty()) {
            throw new NoSuchEntityException("There are no chats for this user");
        }
        return listOfChats;
    }

    @PostMapping("messages/get")
    public List<Message> getAllMessagesOfChat(@RequestBody ChatHolder chatHolder) {
        Chat chat = chatService.getChatById(chatHolder.getChatId());
        if (chat == null) {
            throw new NoSuchEntityException("There is no such chat");
        }
        List<Message> listOfMessages = messageService.getMessagesFromChat(chat);
        if (listOfMessages == null || listOfMessages.isEmpty()) {
            throw new NoSuchEntityException("There are no messages in this chat");
        }

        return listOfMessages;
    }

//    @DeleteMapping("chats/{id}")
//    public String deleteChat(@PathVariable int id) {
//        chatService.deleteChat(id);
//        return "Chat with ID = " + id +" was deleted";
//    }
}
