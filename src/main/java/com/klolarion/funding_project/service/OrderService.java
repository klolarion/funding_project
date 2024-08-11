package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.OrderList;

import java.util.List;

public interface OrderService {
    boolean createOrder();
    List<OrderList> getAllOrderList();

}
