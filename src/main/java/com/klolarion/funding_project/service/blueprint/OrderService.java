package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.OrderList;

import java.util.List;

public interface OrderService {
    /*모든 주문 목록*/
    List<OrderList> getAllOrderList();
    /*내 주문 목록*/
    List<OrderList> myOrderList();
    /*내가 그룹장인 그룹의 주문 목록*/
    List<OrderList> myOwnedGroupsOrderList(Long groupId);

}
