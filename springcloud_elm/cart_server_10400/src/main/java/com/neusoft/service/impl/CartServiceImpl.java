package com.neusoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.neusoft.mapper.CartMapper;
import com.neusoft.po.Cart;
import com.neusoft.po.CommonResult;
import com.neusoft.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Cart> listCart(Cart cart) {
		List<Cart> list = cartMapper.listCart(cart);
		// 调用食品微服务获取每个食品详情
		for (Cart item : list) {
			try {
				CommonResult<?> result = restTemplate.getForObject(
					"http://localhost:10200/FoodController/getFoodById/" + item.getFoodId(),
					CommonResult.class
				);
				if (result != null && result.getCode() == 200 && result.getResult() != null) {
					@SuppressWarnings("unchecked")
					java.util.Map<String, Object> foodMap = (java.util.Map<String, Object>) result.getResult();
					item.setFood(convertToFood(foodMap));
				}
			} catch (Exception e) {
				System.out.println("获取食品信息失败: " + e.getMessage());
			}
		}
		return list;
	}
	
	private com.neusoft.po.Food convertToFood(java.util.Map<String, Object> map) {
		com.neusoft.po.Food food = new com.neusoft.po.Food();
		if (map.get("foodId") != null) food.setFoodId((Integer) map.get("foodId"));
		if (map.get("foodName") != null) food.setFoodName((String) map.get("foodName"));
		if (map.get("foodPrice") != null) food.setFoodPrice((Double) map.get("foodPrice"));
		if (map.get("foodImg") != null) food.setFoodImg((String) map.get("foodImg"));
		if (map.get("businessId") != null) food.setBusinessId((Integer) map.get("businessId"));
		if (map.get("foodExplain") != null) food.setFoodExplain((String) map.get("foodExplain"));
		return food;
	}
	
	@Override
	public int saveCart(Cart cart) {
		return cartMapper.saveCart(cart);
	}
	
	@Override
	public int updateCart(Cart cart) {
		return cartMapper.updateCart(cart);
	}
	
	@Override
	public int removeCart(Cart cart) {
		return cartMapper.removeCart(cart);
	}
}
