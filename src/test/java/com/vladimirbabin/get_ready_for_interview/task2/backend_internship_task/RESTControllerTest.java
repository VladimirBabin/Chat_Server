package com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.ChatRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.MessageRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.dao.UserRepository;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Chat;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.Message;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.entity.User;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.exception_handling.NoSuchEntityException;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.ChatService;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.MessageService;
import com.vladimirbabin.get_ready_for_interview.task2.backend_internship_task.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class RESTControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    UserService userService;

    @Mock
    ChatService chatService;

    @Mock
    MessageService messageService;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

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

    @Autowired
    ObjectMapper objectMapper;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeAll
    public static void setup() {
        request = new MockHttpServletRequest();
    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlCreateChat);
        jdbc.execute(sqlPopulateChat);
        jdbc.execute(sqlAddMessage);
    }

    @Test
    public void getUsersHttpRequest() throws Exception {

        User boris = new User("Boris");
        User petya = new User("Petya");
        User michele = new User("Michele");

        entityManager.persist(boris);
        entityManager.persist(petya);
        entityManager.persist(michele);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));

    }


    @Test
    public void getUserByIdHttpRequest() throws Exception {

        Optional<User> user = userRepository.findById(9);

        assertTrue(user.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.userName", is("Kolya")));
    }

    @Test
    public void addUserHttpRequest() throws Exception {
        User boris = new User("Boris");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boris)))
                .andExpect(status().isOk());

        User verifyUser = userRepository.findByUserName("Boris");
        assertNotNull(verifyUser, "User should be valid");
    }

    @Test
    public void updateUserHttpRequest() throws Exception {

        Optional<User> user = userRepository.findById(9);
        assertTrue(user.isPresent());
        User beforeUpdateUser = user.get();
        assertEquals(beforeUpdateUser.getUserName(), "Kolya", "User with id number 9 name is Kolya");

        User updatedUser = new User("Boris");
        updatedUser.setId(9);


        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("9"));

        updatedUser = userRepository.findById(9).get();
        assertEquals(updatedUser.getUserName(), "Boris", "New name should be Boris");
    }

    @Test
    public void deleteUserHttpRequest() throws Exception {
        assertTrue(userRepository.findById(9).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("User with ID = 9 was deleted"));
    }

    @Test
    public void deleteUserHttpRequestErrorPage() throws Exception {
        assertFalse(userRepository.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{userId}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getChatsHttpRequest() throws Exception {
        Chat falimy = new Chat("Family");
        Chat friends = new Chat("Friends");
        Chat university = new Chat("University");

        entityManager.persist(falimy);
        entityManager.persist(friends);
        entityManager.persist(university);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/chats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));

    }

    @Test
    public void getChatByIdHttpRequest() throws Exception {

        Optional<Chat> chat = chatRepository.findById(9);

        assertTrue(chat.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/chats/{chatId}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.name", is("Security")));
    }

    @Test
    public void saveNewChatBetweenUsersHttpRequest() throws Exception {
        Chat family = new Chat("Family");
        User mother = new User("Mother");
        User father = new User("Father");
        User child = new User("Child");
        family.addUserToChat(mother);
        family.addUserToChat(father);
        family.addUserToChat(child);

        entityManager.persist(mother);
        entityManager.persist(father);
        entityManager.persist(child);

        mockMvc.perform(post("/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(family)))
                .andExpect(status().isOk());

        Chat verifyChat = chatRepository.findByName("Family");
        assertNotNull(verifyChat, "Chat should be valid");
        assertTrue(verifyChat.getUsers().size() == 3, "Chat should have 3 users");

    }

    @Test
    public void updateChatBetweenUsersHttpRequest() throws Exception {

        Optional<Chat> chat = chatRepository.findById(9);
        assertTrue(chat.isPresent());
        Chat beforeUpdateChat = chat.get();
        assertEquals(beforeUpdateChat.getName(), "Security", "Chat with id number 9 name is Security");

        List<User> beforeUpdateChatUsers = beforeUpdateChat.getUsers();

        Chat updatedChat = new Chat("Security_department");
        updatedChat.setId(9);
        for(User user: beforeUpdateChatUsers) {
            updatedChat.addUserToChat(user);
        }

        mockMvc.perform(put("/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedChat)))
                .andExpect(status().isOk())
                .andExpect(content().string("9"));

        updatedChat = chatRepository.findById(9).get();
        assertEquals(updatedChat.getName(), "Security_department", "New name should be Security_department");
        assertEquals(updatedChat.getUsers(), beforeUpdateChatUsers, "Updated chat should have same users");
    }

    @Test
    public void deleteChatHttpRequest() throws Exception {
        assertTrue(chatRepository.findById(9).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/chats/{chatId}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Chat with ID = 9 was deleted"));
    }

    @Test
    public void getAllChatsOfUserHttpRequest() throws Exception {
        Optional<User> userOptional = userRepository.findById(9);

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();

        mockMvc.perform(post("/chats/get_by_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(result -> assertTrue(result.getResponse()
                        .getContentAsString().contains("\"name\":\"Security\"")));
    }

    @Test
    public void deleteChatHttpRequestErrorPage() throws Exception {
        assertFalse(chatRepository.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/chat/{chatId}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMessageByIdHttpRequest() throws Exception {
        Optional<Message> optionalMessage = messageRepository.findById(9);
        assertTrue(optionalMessage.isPresent());
        Message message = optionalMessage.get();

        Chat chat = message.getChat();
        User author = message.getAuthor();

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/{messageId}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.text", is("Hi, Security! My name is Kolya")));
    }

    @Test
    public void addNewMessageFromUserToChatHttpRequest() throws Exception {
        User courier = new User("Courier");
        Chat trackPackage = new Chat("Track_Package");
        Message message = new Message(trackPackage, courier, "The parcel has arrived");

        entityManager.persist(courier);
        entityManager.persist(trackPackage);
        entityManager.persist(message);

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk());

        List<Message> verifyListOfMessages = messageRepository.findAllByChat(trackPackage);
        assertNotNull(verifyListOfMessages, "List of messages should be valid");
        assertTrue(verifyListOfMessages.size() == 1, "List should have 1 message");
        assertTrue(verifyListOfMessages.contains(message), "List should have our exact message");
    }

    @Test
    public void updateMessageFromUserToChatHttpRequest() throws Exception {
        Optional<Message> messageOptional = messageRepository.findById(9);
        assertTrue(messageOptional.isPresent());
        Message beforeUpdateMessage = messageOptional.get();
        assertEquals(beforeUpdateMessage.getText(), "Hi, Security! My name is Kolya",
                "Checking the initial message text");

        Message updatedMessage = new Message(beforeUpdateMessage.getChat(), beforeUpdateMessage.getAuthor(),
                "Hi, guys! Let's get to work");
        updatedMessage.setId(9);

        mockMvc.perform(put("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMessage)))
                .andExpect(status().isOk())
                .andExpect(content().string("9"));

        updatedMessage = messageRepository.findById(9).get();
        assertEquals(updatedMessage.getText(), "Hi, guys! Let's get to work",
                "Text of the message should change");
    }

    @Test
    public void getAllMessagesOfChatHttpRequest() throws Exception {
        Optional<Chat> chatOptional = chatRepository.findById(9);
        assertTrue(chatOptional.isPresent());
        Chat chat = chatOptional.get();

        mockMvc.perform(post("/messages/get_by_chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(result -> assertTrue(result.getResponse()
                        .getContentAsString().contains("\"Hi, Security! My name is Kolya\"")));
    }

    @Test
    public void deleteMessageHttpRequest() throws Exception {
        assertTrue(messageRepository.findById(9).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/{messageId}", 9))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Message with ID = 9 was deleted"));
    }

    @Test
    public void deleteMessageHttpRequestErrorPage() throws Exception {
        assertFalse(messageRepository.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/message/{messageId}", 0))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteMessage);
        jdbc.execute(sqlDeleteUserChat);
        jdbc.execute(sqlDeleteChat);
        jdbc.execute(sqlDeleteUser);
    }
}
