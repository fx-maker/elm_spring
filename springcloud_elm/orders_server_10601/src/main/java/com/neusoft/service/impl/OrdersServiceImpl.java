package com.neusoft.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neusoft.feign.WalletFeignClient;
import com.neusoft.mapper.BusinessMapper;
import com.neusoft.mapper.CartMapper;
import com.neusoft.mapper.FoodMapper;
import com.neusoft.mapper.OrderDetailetMapper;
import com.neusoft.mapper.OrdersMapper;
import com.neusoft.po.Business;
import com.neusoft.po.Cart;
import com.neusoft.po.CommonResult;
import com.neusoft.po.Food;
import com.neusoft.po.OrderDetailet;
import com.neusoft.po.Orders;
import com.neusoft.service.OrdersService;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailetMapper orderDetailetMapper;
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private WalletFeignClient walletFeignClient;

    @Override
    @Transactional
    public int createOrders(Orders orders) {
        // 1、查询当前用户购物车中当前商家的所有食品
        Cart cart = new Cart();
        cart.setUserId(orders.getUserId());
        cart.setBusinessId(orders.getBusinessId());
        List<Cart> cartList = cartMapper.listCart(cart);

        // 2、后端自己计算订单总价 = 食品总价 + 配送费
        Double orderTotal = 0.0;
        for (Cart c : cartList) {
            orderTotal += c.getFood().getFoodPrice() * c.getQuantity();
        }
        Business business = businessMapper.getBusinessById(orders.getBusinessId());
        if (business != null && business.getDeliveryPrice() != null) {
            orderTotal += business.getDeliveryPrice();
        }
        orders.setOrderTotal(orderTotal);

        // 3、创建订单（返回生成的订单编号）
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        orders.setOrderDate(currentDate);
        ordersMapper.saveOrders(orders);
        int orderId = orders.getOrderId();

        // 4、批量添加订单明细
        List<OrderDetailet> list = new ArrayList<>();
        for (Cart c : cartList) {
            OrderDetailet od = new OrderDetailet();
            od.setOrderId(orderId);
            od.setFoodId(c.getFoodId());
            od.setQuantity(c.getQuantity());
            list.add(od);
        }
        orderDetailetMapper.saveOrderDetailetBatch(list);

        // 5、从购物车表中删除相关食品信息
        cartMapper.removeCart(cart);

        return orderId;
    }

    @Override
    public Orders getOrdersById(Integer orderId) {
        Orders order = ordersMapper.getOrdersById(orderId);
        if (order != null) {
            Business business = businessMapper.getBusinessById(order.getBusinessId());
            order.setBusiness(business);
            List<OrderDetailet> detailets = orderDetailetMapper.listOrderDetailetByOrderId(order.getOrderId());
            for (OrderDetailet od : detailets) {
                Food food = foodMapper.getFoodById(od.getFoodId());
                od.setFood(food);
            }
            order.setList(detailets);
        }
        return order;
    }

    @Override
    public List<Orders> listOrdersByUserId(String userId) {
        List<Orders> orderList = ordersMapper.listOrdersByUserId(userId);
        for (Orders order : orderList) {
            Business business = businessMapper.getBusinessById(order.getBusinessId());
            order.setBusiness(business);
            List<OrderDetailet> detailets = orderDetailetMapper.listOrderDetailetByOrderId(order.getOrderId());
            for (OrderDetailet od : detailets) {
                Food food = foodMapper.getFoodById(od.getFoodId());
                od.setFood(food);
            }
            order.setList(detailets);
        }
        return orderList;
    }

    @Override
    public int payOrders(Integer orderId) {
        Orders order = ordersMapper.getOrdersById(orderId);
        if (order == null) {
            return 0;
        }
        CommonResult result = walletFeignClient.pay(order.getUserId(), order.getOrderTotal());
        if (result.getCode() == 200) {
            return ordersMapper.updateOrderState(orderId);
        }
        return 0;
    }
}
