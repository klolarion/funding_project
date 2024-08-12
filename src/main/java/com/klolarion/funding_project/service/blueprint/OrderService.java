package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.OrderList;

import java.util.List;

public interface OrderService {
    List<OrderList> getAllOrderList();
    List<OrderList> myOrderList();
    List<OrderList> groupOrderList(Long groupId);

}
