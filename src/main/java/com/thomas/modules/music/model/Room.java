package com.thomas.modules.music.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private static final int QUEUE_LIMIT = 100;
    private Long ownerId;
    private Queue<Long> trackQueue;
    private Queue<RoomMessage> messageQueue;
    private List<Long> users;

    public Room(Long userId) {
        this.ownerId = userId;
        this.trackQueue = new ConcurrentLinkedQueue<>();
        this.messageQueue = new ConcurrentLinkedQueue<>();
        this.users = new CopyOnWriteArrayList<>();
        this.users.add(ownerId);
    }

    void addUser(Long myId, Long userId) {
        if (!myId.equals(ownerId)) return;
        users.add(userId);
    }

    void removeUser(Long myId, Long userId) {
        if (!myId.equals(ownerId)) return;
        users.remove(userId);
    }

    void sendMessage(Long myId, String content) {
        if (!users.contains(myId)) return;
        if (messageQueue.size() > QUEUE_LIMIT) {
            messageQueue.poll();
        }
        messageQueue.add(new RoomMessage(myId, content));
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<Long> getTrackList() {
        return new ArrayList<>(trackQueue);
    }

    public List<RoomMessage> getMessageList() {
        return new ArrayList<>(messageQueue);
    }
    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Room{" +
                "ownerId=" + ownerId +
                ", trackQueue=" + trackQueue.toString() +
                ", messageQueue=" + messageQueue.toString() +
                ", users=" + users.toString() +
                '}';
    }
}
