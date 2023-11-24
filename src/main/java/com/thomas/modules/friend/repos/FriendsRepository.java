package com.thomas.modules.friend.repos;

import com.thomas.modules.friend.entity.FriendsEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.friend.entity.key.FriendsKey;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<FriendsEntity, FriendsKey>, JpaSpecificationExecutor<FriendsEntity> {
    Optional<FriendsEntity> findByMeAndFriend(UserEntity me, UserEntity friend);
    default Page<UserEntity> findFriendsByUserId(Long userId, Pageable pageable) {
        Specification<FriendsEntity> getFriends = getFriendsAndOffers(userId).and((root, query, cb) -> cb.isNotNull(root.get("confirmedDate")));
        Page<FriendsEntity> friends = findAll(getFriends, pageable);
        return friends.map(FriendsEntity::getFriend);
    }

    default Page<UserEntity> findOffersByUserId(Long userId, Pageable pageable) {
        Specification<FriendsEntity> getOffers = getFriendsAndOffers(userId).and((root, query, cb) -> cb.isNull(root.get("confirmedDate")));
        Page<FriendsEntity> offers = findAll(getOffers, pageable);
        return offers.map(FriendsEntity::getFriend);
    }

    private static Specification<FriendsEntity> getFriendsAndOffers(Long userId) {
        return (root, query, cb) -> {
            Join<FriendsEntity, UserEntity> userJoin = root.join("me");
            return cb.equal(userJoin.get("userId"), userId);
        };
    }
}
