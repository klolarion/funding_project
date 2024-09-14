package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.FriendStatus;
import com.klolarion.funding_project.dto.friend.FriendRequestDto;
import com.klolarion.funding_project.dto.friend.SearchFriendDto;

import java.util.List;

public interface FriendService {

    /*친구추가*/
    FriendStatus addFriend(Long accepterId);

    /*이름으로 친구 검색
    * 나와 친구가 아니고 -> friend테이블에 없어야함
    * 이미 수락된 요청이 있는지 확인 -> friend_status accepted확인
    * */
    List<SearchFriendDto> searchFriend(String searchName);

    /*친구 목록*/
    List<SearchFriendDto> myFriendList();

    /*친구 요청 목록*/
    List<FriendRequestDto> requestList();

    /*친구요청 수락*/
    boolean acceptFriendRequest(Long friendId);

    /*친구삭제*/
    boolean removeFriend(Long friendId);

    /*친구 차단 or 친구요청 거부시 차단*/
    boolean banMember(Long memberId);
}
