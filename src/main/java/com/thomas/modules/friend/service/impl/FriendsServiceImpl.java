package com.thomas.modules.friend.service.impl;

import com.thomas.modules.friend.entity.FriendsEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.friend.repos.FriendsRepository;
import com.thomas.modules.friend.service.FriendsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FriendsServiceImpl implements FriendsService {
    private final FriendsRepository friendsRepository;

    @Autowired
    public FriendsServiceImpl(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Override
    public Page<UserEntity> getFriends(UserEntity user, Pageable pageable) {
        return friendsRepository.findFriendsByUserId(user.getUserId(), pageable);
    }

    @Override
    public Page<UserEntity> getOffers(UserEntity user, Pageable pageable) {
        return friendsRepository.findOffersByUserId(user.getUserId(), pageable);
    }

    @Override
    public void sendOffer(UserEntity from, UserEntity to) {
        friendsRepository.saveAndFlush(new FriendsEntity(to, from));
    }

    @Override
    public FriendsEntity getOffer(UserEntity from, UserEntity to) {
        Optional<FriendsEntity> offer = friendsRepository.findByMeAndFriend(to, from);
        if (offer.isEmpty()) throw new NoSuchElementException("no offer sent from user1Id: " + from.getUserId() + " to user2Id: " + to.getUserId());
        return offer.get();
    }

    //I accept an offer from someone
    @Override
    @Transactional
    public void acceptOffer(FriendsEntity offer) {
        Timestamp now = Timestamp.from(Instant.now());
        offer.setConfirmedDate(now);
        FriendsEntity reversedOffer = new FriendsEntity(offer.getFriend(), offer.getMe());
        reversedOffer.setConfirmedDate(now);
        friendsRepository.saveAllAndFlush(List.of(offer, reversedOffer));
    }

    //I reject an offer from someone
    @Override
    @Transactional
    public void rejectOffer(FriendsEntity offer) {
        friendsRepository.delete(offer);
    }

    @Override
    @Transactional
    public void deleteFriend(UserEntity from, UserEntity to) {
        FriendsEntity offer = getOffer(to, from);
        FriendsEntity reversedOffer = getOffer(from, to);
        friendsRepository.deleteAll(List.of(offer, reversedOffer));
    }
}
