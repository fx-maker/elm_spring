package com.neusoft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neusoft.po.Food;

@Mapper
public interface FoodMapper {

	@Select("select * from food where businessId=#{businessId} order by foodId")
	public List<Food> listFoodByBusinessId(Integer businessId);

	@Select("select * from food where foodId=#{foodId}")
	public Food getFoodById(Integer foodId);

	@Insert("insert into food(foodName, foodExplain, foodImg, foodPrice, businessId, remarks) values(#{foodName}, #{foodExplain}, #{foodImg}, #{foodPrice}, #{businessId}, #{remarks})")
	public int saveFood(Food food);

	@Update("update food set foodName=#{foodName}, foodExplain=#{foodExplain}, foodImg=#{foodImg}, foodPrice=#{foodPrice}, remarks=#{remarks} where foodId=#{foodId}")
	public int updateFood(Food food);

	@Delete("delete from food where foodId=#{foodId}")
	public int deleteFood(Integer foodId);
}
