package com.neusoft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import com.neusoft.po.Cart;

@Mapper
public interface CartMapper {

	@Select("<script>" +
			"SELECT c.cartId, c.foodId, c.businessId, c.userId, c.quantity," +
			" f.foodId AS f_foodId, f.foodName AS f_foodName, f.foodExplain AS f_foodExplain," +
			" f.foodImg AS f_foodImg, f.foodPrice AS f_foodPrice," +
			" b.businessId AS b_businessId, b.businessName AS b_businessName," +
			" b.businessImg AS b_businessImg, b.deliveryPrice AS b_deliveryPrice," +
			" b.starPrice AS b_starPrice" +
			" FROM cart c" +
			" LEFT JOIN food f ON c.foodId = f.foodId" +
			" LEFT JOIN business b ON c.businessId = b.businessId" +
			" WHERE c.userId=#{userId}" +
			"<if test='businessId!=null and businessId!=\"\"'>" +
			" AND c.businessId=#{businessId}" +
			"</if>" +
			" ORDER BY c.cartId" +
			"</script>")
	@Results({
		@Result(column = "f_foodId", property = "food.foodId"),
		@Result(column = "f_foodName", property = "food.foodName"),
		@Result(column = "f_foodExplain", property = "food.foodExplain"),
		@Result(column = "f_foodImg", property = "food.foodImg"),
		@Result(column = "f_foodPrice", property = "food.foodPrice"),
		@Result(column = "b_businessId", property = "business.businessId"),
		@Result(column = "b_businessName", property = "business.businessName"),
		@Result(column = "b_businessImg", property = "business.businessImg"),
		@Result(column = "b_deliveryPrice", property = "business.deliveryPrice"),
		@Result(column = "b_starPrice", property = "business.starPrice"),
	})
	List<Cart> listCart(Cart cart);

	@Insert("INSERT INTO cart VALUES(NULL,#{foodId},#{businessId},#{userId},1)")
	int saveCart(Cart cart);

	@Update("UPDATE cart SET quantity=#{quantity} WHERE foodId=#{foodId} AND businessId=#{businessId} AND userId=#{userId}")
	int updateCart(Cart cart);

	@Delete("<script>DELETE FROM cart WHERE userId=#{userId}" +
			"<if test='businessId!=null and businessId!=\"\"'> AND businessId=#{businessId}</if>" +
			"<if test='foodId!=null and foodId!=\"\"'> AND foodId=#{foodId}</if>" +
			"</script>")
	int removeCart(Cart cart);
}
