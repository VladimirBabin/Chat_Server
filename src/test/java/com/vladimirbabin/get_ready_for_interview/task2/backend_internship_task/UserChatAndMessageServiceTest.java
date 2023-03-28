package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task;

import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.ChatRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.MessageRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.UserRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.NoSuchEntityException;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.ChatServiceImplementation;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.MessageServiceImplementation;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.UserServiceImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserChatAndMessageServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserServiceImplementation userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatServiceImplementation chatService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageServiceImplementation messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Value("${sql.script.create.user}")
    private String sqlAddUser;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.create.chat}")
    private String sqlCreateChat;

    @Value("${sql.script.populate.chat}")
    private String sqlPopulateChat;

    @Value("${sql.script.add.message}")
    private String sqlAddMessage;

    @Value("${sql.script.delete.chat}")
    private String sqlDeleteChat;

    @Value("${sql.script.delete.user_chat}")
    private String sqlDeleteUserChat;

    @Value("${sql.script.delete.message}")
    private String sqlDeleteMessage;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlCreateChat);
        jdbc.execute(sqlPopulateChat);
        jdbc.execute(sqlAddMessage);

    }

    @Test
    public void checkIfUserIsPresent() {

        assertNotNull(userService.findById(9), "@BeforeTransaction creates user : return true");

        assertThrows(NoSuchEntityException.class, () -> {userService.findById(0); }, "No user should be found by id 0 : return null");
    }

    @Test
    public void createUser() {
        userService.save(new User("Vasya"));

        User user = userRepository.findByUserName("Vasya");
        assertEquals("Vasya", user.getUserName(), "Find user by name");
    }

    @Test
    @Transactional
    public void deleteUser() {
        Optional<User> deletedUser = userRepository.findById(9);

        assertTrue(deletedUser.isPresent(), "User with 9 one exists : return true");

        List<Chat> userChats = deletedUser.get().getChats();
        for (Chat chat: userChats) {
            chatService.deleteById(chat.getId());
        }

        userService.deleteById(9);

        deletedUser = userRepository.findById(9);

        assertFalse(deletedUser.isPresent(), "No user with id 9 after deletion : return false");
    }

    @Sql("/insertUsers.sql")
    @Test
    public void findAllPopulatedUsers() {
        List<User> users = userService.findAll();

        assertEquals(5, users.size(), "Must be 5 users in a list");

    }

    @Test
    public void checkIfChatIsPresent() {
        assertNotNull(chatService.findById(9), "@BeforeTransaction creates chat: return true");

        assertThrows(NoSuchEntityException.class, () -> {chatService.findById(0); },
                "No chat should be found by id 0 : return null");
    }

    @Test
    public void createChat() {
        chatService.save(new Chat("Business"));

        Chat chat = chatService.findByName("Business");
        assertEquals("Business", chat.getName(), "Find chat by name");
    }

    @Test
    @Transactional
    public void deleteChat() {
        Optional<Chat> deletedChat = chatRepository.findById(9);

        assertTrue(deletedChat.isPresent(), "Chat with 9 one exists : return true");

        List<Message> chatMessages = deletedChat.get().getMessages();
        for (Message message: chatMessages) {
            messageService.deleteById(message.getId());
        }
        chatService.deleteById(9);

        deletedChat = chatRepository.findById(9);

        assertFalse(deletedChat.isPresent(), "No chat with id 9 after deletion : return false");
    }

    @Sql("/insertChats.sql")
    @Test
    public void findAllPopulatedChats() {
        List<Chat> chats = chatService.findAll();

        assertEquals(5, chats.size(), "Must be 5 chats in a list");

    }

   @Test
   @Transactional
   public void findChatsByUserId() {
        User viktor = new User("Viktor");
        Chat family = new Chat("Family");
        Chat friends = new Chat("Friends");

        viktor.addChatToUser(family);
        viktor.addChatToUser(friends);

        userService.save(viktor);
        chatService.save(family);
        chatService.save(friends);

        List<Chat> chats = chatService.findAllByUserId(userRepository.findByUserName("Viktor").getId());
        assertTrue(chats.size() == 2, "There must be two chats for current user");
        assertTrue(chats.contains(family), "There must be Family chat for current user");
        assertTrue(chats.contains(friends), "There must be Friends chat for current user");
   }


   @Test
   public void checkIfMessageIsPresent() {
       assertNotNull(messageService.findById(9), "@BeforeTransaction creates message: return true");

       assertThrows(NoSuchEntityException.class, () -> {messageService.findById(0); },
               "No message should be found by id 0 : return null");
   }

   @Test
   @Transactional
   public void createMessage() {
        User user = new User();
        Chat chat = new Chat("Business");
        user.addChatToUser(chat);
        Message message = new Message(chat, user, "Hi everyone, this is my message");

        userRepository.save(user);
        chatRepository.save(chat);
        messageRepository.save(message);

        int messageId = messageService.findAllByChat(chatService.findByName("Business")).get(0).getId();
        message = messageService.findById(messageId);
        assertEquals("Hi everyone, this is my message", message.getText(),
                "Text of the messages should be equal");
   }

    @Test
    public void deleteMessage() {
        Optional<Message> deletedMessage = messageRepository.findById(9);

        assertTrue(deletedMessage.isPresent(), "Message with 9 one exists : return true");

        messageService.deleteById(9);

        deletedMessage = messageRepository.findById(9);

        assertFalse(deletedMessage.isPresent(), "No chat with id 9 after deletion : return false");
    }

    @Test
    @Transactional
    public void findAllMessagesForChat () {
        User zigimantas = new User();
        User joris = new User();
        Chat friends = new Chat("Friends");

        friends.addUserToChat(joris);
        friends.addUserToChat(zigimantas);

        Message message1 = new Message(friends, joris, "Hi, Zigis!");
        Message message2 = new Message(friends, zigimantas, "Hi, Joris!");
        Message message3 = new Message(friends, joris, "How was your day?");
        Message message4 = new Message(friends, zigimantas, "Great, thanks!");

        userRepository.save(zigimantas);
        userRepository.save(joris);
        chatRepository.save(friends);
        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        messageRepository.save(message4);

        List<Message> messages = messageService.findAllByChat(friends);
        assertTrue(messages.size() == 4);
        assertTrue(messages.contains(message1));
        assertTrue(messages.contains(message2));
        assertTrue(messages.contains(message3));
        assertTrue(messages.contains(message4));

    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteMessage);
        jdbc.execute(sqlDeleteUserChat);
        jdbc.execute(sqlDeleteChat);
        jdbc.execute(sqlDeleteUser);
    }
}
