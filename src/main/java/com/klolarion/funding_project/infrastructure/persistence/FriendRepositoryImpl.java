package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.domain.entity.Friend;
import com.klolarion.funding_project.application.port.out.FriendRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendRepositoryImpl implements FriendRepository {
    @Override
    public List<Friend> myFriends() {
        return null;
    }

    @Override
    public boolean addFriend() {
        return false;
    }

    @Override
    public boolean acceptRequest() {
        return false;
    }

    @Override
    public boolean denyRequest() {
        return false;
    }

    @Override
    public boolean removeFriend() {
        return false;
    }

    @Override
    public boolean banFriend() {
        return false;
    }
}
