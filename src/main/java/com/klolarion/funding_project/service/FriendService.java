package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Friend;
import com.klolarion.funding_project.dto.FriendDto;

import java.util.List;

public interface FriendService {

    /*친구추가*/
    Friend addFriend(Long accepterId);

    /*친구 검색*/
    List<FriendDto> searchFriend();

    /*친구 목록*/
    List<FriendDto> friendList();

    /*친구 요청 목록*/
    List<FriendDto> requestList();

    /*친구요청 수락*/
    boolean acceptFriendRequest(Long friendId);

    /*친구삭제*/
    boolean removeFriend(Long friendId);

    /*친구 차단 or 친구요청 거부시 차단*/
    boolean banMember(Long memberId);
}
