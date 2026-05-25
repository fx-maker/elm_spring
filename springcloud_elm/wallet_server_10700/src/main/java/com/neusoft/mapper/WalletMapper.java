package com.neusoft.mapper;

import com.neusoft.po.Wallet;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WalletMapper {
	@Select("SELECT * FROM wallet WHERE user_id=#{userId}")
	Wallet getWalletByUserId(@Param("userId") String userId);

	@Insert("INSERT INTO wallet(user_id, money, frozen_money, is_vip, overdraft_limit, overdraft_amount) " +
			"VALUES(#{userId}, #{money}, #{frozenMoney}, #{isVip}, #{overdraftLimit}, #{overdraftAmount})")
	@Options(useGeneratedKeys = true, keyProperty = "walletId", keyColumn = "wallet_id")
	int saveWallet(Wallet wallet);

	@Update("UPDATE wallet SET money=#{money}, frozen_money=#{frozenMoney}, " +
			"is_vip=#{isVip}, overdraft_limit=#{overdraftLimit}, " +
			"overdraft_amount=#{overdraftAmount}, overdraft_time=#{overdraftTime} " +
			"WHERE wallet_id=#{walletId}")
	int updateWallet(Wallet wallet);
}
