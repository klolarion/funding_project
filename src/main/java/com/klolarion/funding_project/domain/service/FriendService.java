package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Friend;

public interface FriendService {

    /*친구추가*/
    Friend addFriend();

    /*친구요청 수락*/
    boolean acceptFriendRequest();

    /*친구삭제*/
    boolean removeFriend();

    /*차단*/
    boolean banMember();
}
