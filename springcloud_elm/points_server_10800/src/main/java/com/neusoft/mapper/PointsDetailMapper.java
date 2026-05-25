package com.neusoft.mapper;

import com.neusoft.po.PointsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PointsDetailMapper {
	List<PointsDetail> listPointsDetailByUserId(@Param("userId") String userId);
	int savePointsDetail(PointsDetail detail);
	int updatePointsDetail(PointsDetail detail);
}
