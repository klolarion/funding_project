package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Friend;

public interface FriendService {
    Friend addFriend();
    boolean acceptFriendRequest();
    boolean removeFriend();
    boolean banMember();
}
