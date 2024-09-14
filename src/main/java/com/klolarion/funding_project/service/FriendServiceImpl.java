package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.friend.FriendRequestDto;
import com.klolarion.funding_project.dto.friend.SearchFriendDto;
import com.klolarion.funding_project.repository.FriendRepository;
import com.klolarion.funding_project.repository.FriendStatusRepository;
import com.klolarion.funding_project.service.blueprint.FriendService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendStatusRepository friendStatusRepository;
    private final CurrentMember currentMember;
    private final EntityManager em;
    private final JPAQueryFactory query;

    @Override
    public FriendStatus addFriend(Long accepterId) {
        Member member = currentMember.getMember();
        QMember qMember = QMember.member;
        Member accepter = query.selectFrom(qMember).where(qMember.memberId.eq(accepterId)).fetchOne();

        FriendStatus friendStatus = new FriendStatus(
                member,
                accepter,
                false,
                false
        );
        FriendStatus saved = friendStatusRepository.save(friendStatus);
        return saved;

    }

    /*이름으로 친구추가할 사용자 검색
     * 나와 친구가 아니고 -> friend테이블에 없어야함
     * 이미 친구요청이 된 상태인지 확인 -> friendStatus accept false인지 확인
     *
     * return->
     * 사용자 id
     * 사용자 name
     *
     * */
    @Override
    public List<SearchFriendDto> searchFriend(String searchName) {
        Member member = currentMember.getMember();
        QFriendStatus qFriendStatus = QFriendStatus.friendStatus;
        QFriend qFriend = QFriend.friend;
        QMember qMember = QMember.member;

        List<SearchFriendDto> searchFriendDtoList = query.select(
                        Projections.constructor(SearchFriendDto.class,
                                qMember.memberId,
                                qMember.memberName
                        ))
                .from(qMember)
                //status requester, accepter 둘 중 하나라도 일치하면 조인
                .join(qFriendStatus).on(qFriendStatus.requester.memberId.eq(member.getMemberId())
                        .or(qFriendStatus.accepter.memberId.eq(member.getMemberId())))
                .where(qFriendStatus.accepted.isFalse() //status accepted false
                        .and(qFriendStatus.denied.isFalse()) //status denied false
                        .and(qMember.memberName.contains(searchName))//검색이름으로 필터링
                .and(JPAExpressions.selectOne() // friend에 없어야함
                        .from(qFriend)
                        .where(qFriend.requester.memberId.eq(qFriendStatus.requester.memberId)
                                .and(qFriend.accepter.memberId.eq(qFriendStatus.accepter.memberId)))
                        .notExists()))
                .fetch();

        em.flush();
        em.clear();

        return searchFriendDtoList;
    }

    @Override
    public List<SearchFriendDto> myFriendList() {
        Member member = currentMember.getMember();
        QFriend qFriend = QFriend.friend;
        QMember qMember = QMember.member;

        List<SearchFriendDto> friends = query
                .select(Projections.constructor(SearchFriendDto.class,
                        qMember.memberId,
                        qMember.memberName
                ))
                .from(qFriend)
                .join(qMember).on(
                        qFriend.accepter.eq(qMember).or(qFriend.requester.eq(qMember))
                )
                .where(
                        (qFriend.accepter.memberId.eq(member.getMemberId())
                                .or(qFriend.requester.memberId.eq(member.getMemberId())))
                                .and(qFriend.deleted.isFalse())
                                .and(qFriend.banned.isFalse())
                )
                .fetch();
        em.flush();
        em.clear();
        return friends;
    }

    @Override
    public List<FriendRequestDto> requestList() {
        Member member = currentMember.getMember();
        QFriendStatus qFriendStatus = QFriendStatus.friendStatus;

        List<FriendRequestDto> friendRequests = query.select(Projections.constructor(FriendRequestDto.class,
                        qFriendStatus.friendStatusId,
                        qFriendStatus.requester.memberId,
                        qFriendStatus.requester.memberName))
                .from(qFriendStatus)
                .where(
                        qFriendStatus.accepter.memberId.eq(member.getMemberId())
                                .and(qFriendStatus.accepted.isFalse())
                                .and(qFriendStatus.denied.isFalse())
                )
                .fetch();
        em.flush();
        em.clear();
        return friendRequests;
    }

    @Override
    public boolean acceptFriendRequest(Long friendStatusId) {
        QFriendStatus qFriendStatus = QFriendStatus.friendStatus;

        //friendStatus 업데이트
        long result = query.update(qFriendStatus).set(qFriendStatus.accepted, true).execute();

        //새로운 친구객체 생성
        if(result == 1L) {
            Tuple tuple = query.select(
                            qFriendStatus.requester,
                            qFriendStatus.accepter
                    ).from(qFriendStatus)
                    .where(qFriendStatus.friendStatusId.eq(friendStatusId))
                    .fetchOne();
            Friend friend = new Friend(
                tuple.get(qFriendStatus.requester),
                tuple.get(qFriendStatus.accepter)
            );
            friendRepository.save(friend);
        }
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean removeFriend(Long friendId) {
        QFriend qFriend = QFriend.friend;
        long result = query.update(qFriend).set(qFriend.deleted, true).where(qFriend.friendId.eq(friendId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean banMember(Long friendId) {
        QFriend qFriend = QFriend.friend;
        long result = query.update(qFriend).set(qFriend.banned, true).where(qFriend.friendId.eq(friendId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }
}
