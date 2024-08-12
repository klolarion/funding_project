package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Friend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{
    @Override
    public Friend addFriend() {
        return null;
    }

    @Override
    public boolean acceptFriendRequest() {
        return false;
    }

    @Override
    public boolean removeFriend() {
        return false;
    }

    @Override
    public boolean banMember() {
        return false;
    }
}
