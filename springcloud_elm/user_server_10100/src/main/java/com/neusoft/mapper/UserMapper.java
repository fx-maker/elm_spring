package com.neusoft.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neusoft.po.User;

@Mapper
public interface UserMapper {

	@Select("select * from user where userId=#{userId} and password=#{password}")
	public User getUserByIdByPass(User user);

	@Select("select count(*) from user where userId=#{userId}")
	public int getUserById(String userId);

	@Insert("insert into user values(#{userId},#{password},#{userName},#{userSex},null,1,'USER')")
	public int saveUser(User user);

	@Select("select * from user")
	public java.util.List<User> listAllUsers();

	@Update("update user set userType=#{userType} where userId=#{userId}")
	public int updateUserType(@Param("userId") String userId, @Param("userType") String userType);
}
