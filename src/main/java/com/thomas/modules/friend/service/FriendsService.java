package com.thomas.modules.friend.service;

import com.thomas.modules.friend.entity.FriendsEntity;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FriendsService {
    Page<UserEntity> getFriends(UserEntity user, Pageable pageable);
    Page<UserEntity> getOffers(UserEntity user, Pageable pageable);
    void sendOffer(UserEntity from, UserEntity to);
    FriendsEntity getOffer(UserEntity from, UserEntity to);
    void deleteFriend(UserEntity from, UserEntity to);
    void acceptOffer(FriendsEntity offer);
    void rejectOffer(FriendsEntity offer);

}
