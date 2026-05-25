package com.neusoft.mapper;

import com.neusoft.po.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TransactionMapper {
	@Select("SELECT t.* FROM transaction t " +
			"LEFT JOIN wallet tw ON t.to_wallet = tw.wallet_id " +
			"LEFT JOIN wallet fw ON t.from_wallet = fw.wallet_id " +
			"WHERE tw.user_id = #{userId} OR fw.user_id = #{userId} " +
			"ORDER BY t.transaction_id")
	List<Transaction> listTransactionByUserId(@Param("userId") String userId);

	@Insert("INSERT INTO transaction(money,type,from_wallet,to_wallet,status,order_id,description,create_time) " +
			"VALUES(#{money},#{type},#{fromWallet},#{toWallet},#{status},#{orderId},#{description},#{createTime})")
	@Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transaction_id")
	int saveTransaction(Transaction transaction);

	@Update("UPDATE transaction SET status=#{status} WHERE transaction_id=#{transactionId}")
	int updateTransaction(Transaction transaction);
}
