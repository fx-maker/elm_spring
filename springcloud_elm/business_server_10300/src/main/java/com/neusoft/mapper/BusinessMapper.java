package com.neusoft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neusoft.po.Business;

@Mapper
public interface BusinessMapper {

	@Select("select * from business where orderTypeId=#{orderTypeId} order by businessId")
	public List<Business> listBusinessByOrderTypeId(Integer orderTypeId);

	@Select("select * from business where businessId=#{businessId}")
	public Business getBusinessById(Integer businessId);

	@Insert("insert into business(businessName,businessAddress,businessExplain,orderTypeId,starPrice,deliveryPrice,ownerId) values(#{businessName},#{businessAddress},#{businessExplain},1,#{starPrice},#{deliveryPrice},#{ownerId})")
	public int saveBusiness(Business business);

	@Update("update business set ownerId=#{ownerId} where businessId=#{businessId}")
	public int updateOwner(@Param("businessId") Integer businessId, @Param("ownerId") String ownerId);
}
