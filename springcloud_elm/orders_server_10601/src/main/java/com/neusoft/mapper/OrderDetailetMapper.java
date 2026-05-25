package com.neusoft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.neusoft.po.OrderDetailet;

@Mapper
public interface OrderDetailetMapper {

	int saveOrderDetailetBatch(@Param("list") List<OrderDetailet> list);

	List<OrderDetailet> listOrderDetailetByOrderId(@Param("orderId") Integer orderId);
}
