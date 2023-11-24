package com.thomas.modules.friend.entity;

import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.friend.entity.key.FriendsKey;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "friends")
public class FriendsEntity {
    @EmbeddedId
    private FriendsKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("myId")
    @JoinColumn(name = "my_id")
    private UserEntity me;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    private UserEntity friend;

    @Column(name = "confirmed_date")
    private Timestamp confirmedDate;

    public FriendsEntity() {
    }

    public FriendsEntity(UserEntity me, UserEntity user) {
        id = new FriendsKey();
        id.setFriendId(user.getUserId());
        id.setMyId(me.getUserId());
        setMe(me);
        setFriend(user);
    }

    public FriendsKey getId() {
        return id;
    }

    public void setId(FriendsKey id) {
        this.id = id;
    }

    public UserEntity getMe() {
        return me;
    }

    public void setMe(UserEntity me) {
        this.me = me;
    }

    public UserEntity getFriend() {
        return friend;
    }

    public void setFriend(UserEntity friends) {
        this.friend = friends;
    }

    public boolean isConfirmed() {
        return getConfirmedDate() != null;
    }

    public Timestamp getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Timestamp confirmedDate) {
        this.confirmedDate = confirmedDate;
    }


}
