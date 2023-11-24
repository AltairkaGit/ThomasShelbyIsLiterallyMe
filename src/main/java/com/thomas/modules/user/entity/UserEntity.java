package com.thomas.modules.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thomas.modules.appRole.entity.AppRoleEntity;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.ChatUserEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.chat.entity.MessageSeenEntity;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.friend.entity.FriendsEntity;
import com.thomas.modules.server.entity.ServerUserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
public class UserEntity {
    @Schema
    public enum Gender { male, female };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "user_gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "passwd", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_app_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "app_role_id", referencedColumnName = "app_role_id")})
    @JsonManagedReference
    private List<AppRoleEntity> roles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    @JsonManagedReference
    private FileEntity profilePicture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "me")
    @JsonBackReference
    private Set<FriendsEntity> myFriends;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friend")
    @JsonBackReference
    private Set<FriendsEntity> imFriendOf;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonBackReference
    private Set<ChatUserEntity> myChats;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonBackReference
    private Set<MessageSeenEntity> seenMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    @JsonBackReference
    private Set<MessageEntity> messages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonBackReference
    private Set<ServerUserEntity> myServers;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AppRoleEntity> getRoles() {
        return roles == null ? List.of() : roles;
    }

    public Optional<FileEntity> getProfilePicture() {
        if (profilePicture == null) return  Optional.empty();
        return Optional.of(profilePicture);
    }

    public void setRoles(List<AppRoleEntity> roles) {
        this.roles = roles;
    }

    public void setProfilePicture(FileEntity userProfilePicture) {
        this.profilePicture = userProfilePicture;
    }

    public Set<FriendsEntity> getMyFriends() {
        return myFriends;
    }

    public Set<FriendsEntity> getImFriendOf() {
        return imFriendOf;
    }

    public Set<ChatEntity> getMyChats() {
        return myChats.stream().map(ChatUserEntity::getChat).collect(Collectors.toSet());
    }

    public Set<MessageEntity> getMessages() {
        return messages;
    }

    public Set<ServerUserEntity> getMyServers() {
        return myServers;
    }

    public void setMyFriends(Set<FriendsEntity> myFriends) {
        this.myFriends = myFriends;
    }

    public void setImFriendOf(Set<FriendsEntity> imFriendOf) {
        this.imFriendOf = imFriendOf;
    }

    public void setMyChats(Set<ChatUserEntity> myChats) {
        this.myChats = myChats;
    }

    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
    }

    public void setMyServers(Set<ServerUserEntity> myServers) {
        this.myServers = myServers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserEntity user = (UserEntity) obj;
        return user.getUserId().equals(userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, gender, password);
    }
}
