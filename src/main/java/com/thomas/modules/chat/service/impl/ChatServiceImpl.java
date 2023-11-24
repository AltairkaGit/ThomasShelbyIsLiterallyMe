package com.thomas.modules.chat.service.impl;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.ChatUserRoleEntity;
import com.thomas.modules.chat.entity.key.ChatUserKey;
import com.thomas.modules.chat.exception.UserAlreadyInTheChatException;
import com.thomas.modules.chat.exception.UserNotInTheChatException;
import com.thomas.lib.validator.Validator;
import com.thomas.lib.validator.impl.MaxLengthValidation;
import com.thomas.lib.validator.impl.MinLengthValidation;
import com.thomas.lib.validator.impl.ValidatorImpl;
import com.thomas.modules.chat.entity.ChatUserEntity;
import com.thomas.modules.chat.repos.ChatUserRoleRepository;
import com.thomas.modules.chat.repos.MessageRepository;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.security.chatAuthorization.ChatAuthorizationService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.chat.repos.ChatRepository;
import com.thomas.modules.chat.repos.ChatUserRepository;
import com.thomas.modules.user.repos.UserRepository;
import com.thomas.modules.chat.service.ChatService;
import com.thomas.roles.ChatRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatServiceImpl implements ChatService, ChatAuthorizationService {
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatUserRoleRepository chatUserRoleRepository;

    @Autowired
    public ChatServiceImpl(
            ChatRepository chatRepository,
            ChatUserRepository chatUserRepository,
            UserRepository userRepository,
            MessageRepository messageRepository,
            ChatUserRoleRepository chatUserRoleRepository
    ) {
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.chatUserRoleRepository = chatUserRoleRepository;
    }

    @Override
    public ChatEntity getById(Long chatId) {
        Optional<ChatEntity> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) throw new NoSuchElementException("chat with this id is not present");
        return chat.get();
    }

    @Override
    @Transactional
    public ChatEntity createGroupChat(String name, UserEntity creator, Collection<UserEntity> users) {
        ChatEntity chat = new ChatEntity();
        chat.setChatType(ChatEntity.ChatType.conversation);
        if (name != null) {
            validateName(name);
            chat.setChatName(name);
        } else {
            String chatName = users.stream().limit(3)
                    .map(UserEntity::getUsername)
                    .collect(Collectors.joining(", ", "", "..."));
            chat.setChatName(chatName);
        }
        chatRepository.saveAndFlush(chat);

        addUsers(chat, users);
        try {
            addUser(chat, creator);
        } catch (UserAlreadyInTheChatException ignored) {
        }

        ChatUserRoleEntity adminRole = new ChatUserRoleEntity(creator, chat, ChatRole.Admin);
        chatUserRoleRepository.save(adminRole);

        return chat;
    }

    @Override
    @Transactional
    public ChatEntity craeteDirectChat(UserEntity creator, UserEntity partner) throws UserAlreadyInTheChatException {
        ChatEntity chat = new ChatEntity();
        chat.setChatType(ChatEntity.ChatType.direct);

        chatRepository.saveAndFlush(chat);

        addUser(chat, creator);
        addUser(chat, partner);
        ChatUserRoleEntity adminRole = new ChatUserRoleEntity(creator, chat, ChatRole.Admin);
        chatUserRoleRepository.save(adminRole);

        return chat;
    }

    @Override
    public List<ChatRole> getUserChatRoles(ChatEntity chat, UserEntity user) {
        return chatUserRoleRepository.findChatUserRoles(chat.getChatId(), user.getUserId());
    }

    @Override
    public List<ChatRole> getUserChatRoles(Long chatId, Long userId) {
        return chatUserRoleRepository.findChatUserRoles(chatId, userId);
    }

    @Override
    public void addChatUserRole(ChatEntity chat, UserEntity user, ChatRole role) {
        if (!checkUserInChat(chat, user)) throw new IllegalArgumentException("user not in the chat");
        ChatUserRoleEntity adminRole = new ChatUserRoleEntity(user, chat, role);
        chatUserRoleRepository.save(adminRole);
    }

    @Override
    public void delete(ChatEntity chat) {
        chatRepository.delete(chat);
    }

    @Override
    public void deleteById(Long chatId) {
        chatRepository.deleteByChatId(chatId);
    }

    @Override
    public boolean checkUserInChat(ChatEntity chat, UserEntity user) {
        Optional<ChatUserEntity> chatUser = chatUserRepository.findByChatAndUser(chat, user);
        return chatUser.isPresent();
    }

    @Override
    public boolean checkUserInChat(Long chatId, Long userId) {
        Optional<ChatEntity> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) return false;
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) return false;

        return checkUserInChat(chat.get(), user.get());
    }

    @Override
    public void updatePicture(ChatEntity chat, FileEntity picture) {
        chat.setChatPicture(picture);
        chatRepository.saveAndFlush(chat);
    }

    @Override
    public void updateChatName(ChatEntity chat, String chatName) {
        chat.setChatName(chatName);
        chatRepository.saveAndFlush(chat);
    }

    @Override
    public Page<ChatEntity> getUserChats(UserEntity user, Pageable pageable) {
        return chatUserRepository.findUserChats(user, pageable);
    }

    @Override
    public Page<UserEntity> getChatUsers(ChatEntity chat, Pageable pageable) {
        return chatUserRepository.findChatUsers(chat, pageable);
    }

    @Override
    public Stream<UserEntity> getChatUsers(ChatEntity chat) {
        return chatUserRepository.findChatUsers(chat);
    }

    @Override
    public Set<Long> getAllChatUserIds(ChatEntity chat) {
        return chatUserRepository.findAllUserIdsByChat(chat);
    }

    @Override
    public Stream<Long> streamAllChatUserIds(ChatEntity chat) {
        return chatUserRepository.streamAllUserIdsByChat(chat);
    }

    @Override
    public long countUnreadMessages(ChatEntity chat, UserEntity user, Timestamp origin) {
        return messageRepository.countUnreadMessages(chat.getChatId(), user.getUserId(), origin);
    }

    @Override
    public void addUser(ChatEntity chat, UserEntity user) throws UserAlreadyInTheChatException {
        if (checkUserInChat(chat, user))
            throw new UserAlreadyInTheChatException(user.getUsername(), chat.getChatId());
        ChatUserEntity chatUserEntity = new ChatUserEntity(chat, user);
        chatUserRepository.saveAndFlush(chatUserEntity);
    }

    @Override
    public void addUsers(ChatEntity chat, Collection<UserEntity> users) {
        Set<ChatUserEntity> chatUserEntities = users.stream().map(user -> new ChatUserEntity(chat, user)).collect(Collectors.toSet());
        chatUserRepository.saveAllAndFlush(chatUserEntities);
    }

    /** Удаляет участников из чата
     * Если это был direct, то при выходе одного чела чат удаляется автоматически
     * Если групповой, то чат удаляется если в нем не осталось участников
     * -------
     * Если удаляется админ, но назначается новый сначала из модераторов, потом из обычных пользователей
    **/
    @Override
    @Transactional
    public void removeUser(ChatEntity chat, UserEntity user) throws UserNotInTheChatException {
        if (!checkUserInChat(chat, user)) throw new UserNotInTheChatException(user.getUsername(), chat.getChatId());

        chatUserRepository.deleteById(ChatUserKey.valueOf(user.getUserId(), chat.getChatId()));
        //auto delete chat if empty
        if (chat.getChatType().equals(ChatEntity.ChatType.direct)) {
            chatRepository.delete(chat);
        } else if (chat.getChatType().equals(ChatEntity.ChatType.conversation)) {
            if (chatUserRepository.countByChat(chat) > 0) {
                //if admin leave chat we should pick a new one
                Optional<ChatUserRoleEntity> admin = chatUserRoleRepository.findTopByChatAndIdRole(chat, ChatRole.Admin);
                if (admin.isEmpty()) {
                    Optional<ChatUserRoleEntity> moder = chatUserRoleRepository.findTopByChatAndIdRole(chat, ChatRole.Moderator);
                    if (moder.isPresent()) setNewChatAdminDeleteOld(chat, moder.get().getUser());
                    else {
                        UserEntity participant = chatUserRepository.findTopByChat(chat).get().getUser();
                        setNewChatAdminDeleteOld(chat, participant);
                    }
                }
            }
            //no more users in the chat
            else {
                chatRepository.delete(chat);
            }
        }
    }

    @Override
    public UserEntity getChatAdmin(ChatEntity chat) {
        return chatUserRoleRepository.findTopByChatAndIdRole(chat, ChatRole.Admin).get().getUser();
    }

    @Override
    public Set<UserEntity> getChatModerators(ChatEntity chat) {
        return chatUserRoleRepository.findAllByChatAndIdRole(chat, ChatRole.Moderator).stream().map(ChatUserRoleEntity::getUser).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void setNewChatAdminDeleteOld(ChatEntity chat, UserEntity user) {
        Optional<ChatUserRoleEntity> admin = chatUserRoleRepository.findTopByChatAndIdRole(chat, ChatRole.Admin);
        admin.ifPresent(chatUserRoleRepository::delete);
        //deleting old user roles
        chatUserRoleRepository.deleteAllByUserAndChat(user, chat);
        //set up new chat admin role
        ChatUserRoleEntity nextAdmin = new ChatUserRoleEntity(user, chat, ChatRole.Admin);
        chatUserRoleRepository.saveAndFlush(nextAdmin);
    }

    @Override
    public void addChatModerator(ChatEntity chat, UserEntity user) {
        if (Objects.equals(user, getChatAdmin(chat)))
            throw new IllegalArgumentException("the user is admin, can't make it moder");
        ChatUserRoleEntity moder = new ChatUserRoleEntity(user, chat, ChatRole.Moderator);
        chatUserRoleRepository.saveAndFlush(moder);
    }

    @Override
    public void removeChatModerator(ChatEntity chat, UserEntity user) {
        Optional<ChatUserRoleEntity> moder = chatUserRoleRepository.findByChatAndUserAndIdRole(chat, user, ChatRole.Moderator);
        if (moder.isEmpty())
            throw new IllegalArgumentException("the user is not moderator");
        chatUserRoleRepository.delete(moder.get());
    }

    @Override
    public Optional<ChatEntity> getDirectByUsers(UserEntity u1, UserEntity u2) {
        return chatUserRepository.findDirectByUsers(u1, u2);
    }

    @Override
    public long countChatUsers(ChatEntity chat) {
        return chatUserRepository.countByChat(chat);
    }

    @Override
    public void removeUsers(ChatEntity chat, Collection<UserEntity> users) {
        UserEntity admin = getChatAdmin(chat);
        if (users.contains(admin))
            throw new IllegalArgumentException("you can't remove admin from the chat, re pick users to remove");
        chatUserRepository.deleteAllByUserInAndChat(users, chat);
    }

    private void validateName(String name) {
        Validator<String> nameValidator = new ValidatorImpl<>(
                new MinLengthValidation(3).setNextChain(
                        new MaxLengthValidation(40)
                ));
        nameValidator.validate("ChatName", name);
    }
    @Override
    public Long extractChatIdFromURI(String uri) {
        Matcher matcher = chatIdPattern.matcher(uri);
        if (matcher.find()) {
            String chatIdStr = matcher.group(1);
            return Long.parseLong(chatIdStr);
        }
        return null;
    }

    private static final Pattern chatIdPattern = Pattern.compile("/chat/(\\d+)");
}
