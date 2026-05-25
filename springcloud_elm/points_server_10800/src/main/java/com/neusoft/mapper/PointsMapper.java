package com.neusoft.mapper;

import com.neusoft.po.Points;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointsMapper {
	Points getPointsByUserId(@Param("userId") String userId);
	int savePoints(Points points);
	int updatePoints(Points points);
}
