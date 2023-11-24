package com.thomas.modules.friend.entity.key;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class FriendsKey implements Serializable {
    @Column(name = "my_id")
    private Long myId;
    @Column(name = "friend_id")
    private Long friendId;

    public Long getMyId() {
        return myId;
    }

    public void setMyId(Long myId) {
        this.myId = myId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendsKey that = (FriendsKey) o;

        if (!myId.equals(that.myId)) return false;
        return friendId.equals(that.friendId);
    }

    @Override
    public int hashCode() {
        int result = myId.hashCode();
        result = 31 * result + friendId.hashCode();
        return result;
    }
}
