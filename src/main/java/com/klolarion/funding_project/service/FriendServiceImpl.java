package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.FriendDto;
import com.klolarion.funding_project.repository.FriendRepository;
import com.klolarion.funding_project.repository.FriendStatusRepository;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final FriendStatusRepository friendStatusRepository;
    private final CurrentMember currentMember;
    private final EntityManager em;
    private final JPAQueryFactory query;

    @Override
    public Friend addFriend(Long accepterId) {
        Member member = currentMember.getMember();
        QMember qMember = QMember.member;
        Member accepter = query.selectFrom(qMember).where(qMember.memberId.eq(accepterId)).fetchOne();
        Friend friend = new Friend(member, accepter);
        Friend saved = friendRepository.save(friend);
        em.flush();
        em.clear();
        return saved;
    }

    @Override
    public List<FriendDto> searchFriend() {
        return null;
    }

    @Override
    public List<FriendDto> friendList() {
//        Member member = currentMember.getMember();
//        QFriend qFriend = QFriend.friend;
//        QFriendStatus qFriendStatus = QFriendStatus.friendStatus;
//        List<FriendDto> friends = query.select(Projections.constructor(FriendDto.class,
//                        qFriend.friendId,
//                        qFriend.requester.memberId,
//                        qFriend.requester.memberName))
//                .from(qFriend)
//                .where(
//                        qFriend.requester.memberId.eq(member.getMemberId())
//                                .and(qFriend.accepter.memberId.eq(member.getMemberId()))
//                                .and(qFriendStatus..isTrue())
//                )
//                .fetch();
//
//        em.flush();
//        em.clear();
        return null;
    }

    @Override
    public List<FriendDto> requestList() {
        Member member = currentMember.getMember();
        QFriend qFriend = QFriend.friend;

        List<FriendDto> friendRequests = query.select(Projections.constructor(FriendDto.class,
                        qFriend.friendId,
                        qFriend.requester.memberId,
                        qFriend.requester.memberName))
                .from(qFriend)
                .where(
                        qFriend.accepter.memberId.eq(member.getMemberId())


                )
                .fetch();
        em.flush();
        em.clear();
        return friendRequests;
    }

    @Override
    public boolean acceptFriendRequest(Long friendId) {
//        QFriend qFriend = QFriend.friend;
//        long result = query.update(qFriend).set(qFriend.accepted, true).where(qFriend.friendId.eq(friendId)).execute();
//        em.flush();
//        em.clear();
//        return result == 1L;
        return false;
    }

    @Override
    public boolean removeFriend(Long friendId) {
//        QFriend qFriend = QFriend.friend;
//        long result = query.update(qFriend).set(qFriend.deleted, true).where(qFriend.friendId.eq(friendId)).execute();
//        em.flush();
//        em.clear();
//        return result == 1L;
        return false;
    }

    @Override
    public boolean banMember(Long friendId) {
//        QFriend qFriend = QFriend.friend;
//        long result = query.update(qFriend).set(qFriend.banned, true).where(qFriend.friendId.eq(friendId)).execute();
//        em.flush();
//        em.clear();
//        return result == 1L;
        return false;
    }
}
