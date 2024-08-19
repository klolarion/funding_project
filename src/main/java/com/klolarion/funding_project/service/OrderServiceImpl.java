package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.repository.OrderRepository;
import com.klolarion.funding_project.service.blueprint.OrderService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final JPAQueryFactory query;
    private final EntityManager em;
    private final CurrentMember currentMember;
    private final OrderRepository orderRepository;

    @Override
    public List<OrderList> getAllOrderList() {

        QOrderList qOrderList = QOrderList.orderList;
        List<OrderList> orderLists = query.selectFrom(qOrderList).fetch();
        em.flush();
        em.clear();

        return orderLists;
    }

    @Override
    public List<OrderList> myOrderList() {
        Member member = currentMember.getMember();
        QOrderList qOrderList = QOrderList.orderList;
        List<OrderList> orderLists = query.selectFrom(qOrderList).where(qOrderList.member.memberId.eq(member.getMemberId())).fetch();
        em.flush();
        em.clear();
        return orderLists;
    }

    @Override
    public List<OrderList> myOwnedGroupsOrderList(Long groupId) {
        Member member = currentMember.getMember();
        QOrderList qOrderList = QOrderList.orderList;
        QGroup qGroup = QGroup.group;
        List<OrderList> orderLists = query.selectFrom(qOrderList)
                .join(qGroup).on(qGroup.groupLeader.memberId.eq(member.getMemberId())).fetch();
        em.flush();
        em.clear();
        return orderLists;
    }


}
