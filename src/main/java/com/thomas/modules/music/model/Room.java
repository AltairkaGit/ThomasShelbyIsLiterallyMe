package com.thomas.modules.music.model;

import com.thomas.modules.file.service.FileService;
import com.thomas.modules.music.service.TrackService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Room {
    private final SimpMessagingTemplate messagingTemplate;
    private static final int QUEUE_LIMIT = 100;
    private volatile AtomicBoolean paused = new AtomicBoolean(false);
    private Long ownerId;
    private Queue<Long> trackQueue;
    private Queue<RoomMessage> messageQueue;
    private List<Long> users;
    private List<Long> offers;
    private String artifact;
    private StreamingResponseBody stream;
    private BufferedReader reader;

    public Room(Long userId, TrackService trackService, SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.ownerId = userId;
        this.trackQueue = new ConcurrentLinkedQueue<>();
        this.messageQueue = new ConcurrentLinkedQueue<>();
        this.users = new CopyOnWriteArrayList<>();
        this.users.add(ownerId);
        this.offers = new CopyOnWriteArrayList<>();
        this.artifact = UUID.randomUUID().toString();
    }

    public void sendOffer(Long myId, Long userId) {
        if (!myId.equals(ownerId)) return;
        offers.add(userId);
    }

    public void acceptOffer(Long userId) {
        if (offers.contains(userId)) {
            offers.remove(userId);
            users.remove(userId);
            users.add(userId);
        }
    }

    public void addInQueue(Long trackId) {
        trackQueue.add(trackId);
    }

    public void playQueue(FileService fileService) {
        release();
        while (!trackQueue.isEmpty()) {
            Long trackId = getPlayingNow();
            String url = fileService.getFile(trackId).getName();
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileService.urlToFilename(url))));
                stream = outputStream -> {
                    try (outputStream) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.endsWith(".ts") || paused.get()) continue;
                            outputStream.write(line.getBytes());
                            outputStream.write(System.lineSeparator().getBytes());
                        }
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        reader.close();
                        trackQueue.poll();
                    }
                };
                messagingTemplate.convertAndSend("/app/queue/room/" + ownerId + "/tracks", "now:" + trackId);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void pause() {
        paused.set(true);
    }

    public void release() {
        paused.set(false);
    }

    public Long getPlayingNow() {
        return trackQueue.peek();
    }

    public void declineOffer(Long myId) {
        offers.remove(myId);
    }

    public void joinRoom(Long myId) {
        users.remove(myId);
        offers.remove(myId);
        users.add(myId);
    }

    public void removeUser(Long myId, Long userId) {
        if (!myId.equals(ownerId)) return;
        users.removeIf(u -> u.equals(userId));
    }

    public void leave(Long userId) {
        users.remove(userId);
    }

    public RoomMessage sendMessage(Long myId, String content) {
        if (!users.contains(myId)) return null;
        if (messageQueue.size() > QUEUE_LIMIT) {
            messageQueue.poll();
        }
        RoomMessage message = new RoomMessage(myId, content);
        messageQueue.add(message);
        return message;
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

    public List<Long> getOffers() {
        return offers;
    }

    public String getArtifact() {
        return artifact;
    }

    public StreamingResponseBody getStream() {
        return stream;
    }

    public void setStream(StreamingResponseBody stream) {
        this.stream = stream;
    }
}
