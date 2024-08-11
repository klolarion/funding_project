package com.klolarion.funding_project.application.port.out;

import com.klolarion.funding_project.domain.entity.Friend;

import java.util.List;

public interface FriendRepository {

    List<Friend> myFriends();
    boolean addFriend();
    boolean acceptRequest();
    boolean denyRequest();
    boolean removeFriend();
    boolean banFriend();


}
