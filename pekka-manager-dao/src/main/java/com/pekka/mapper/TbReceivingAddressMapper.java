package com.pekka.mapper;

import com.pekka.pojo.TbReceivingAddress;
import com.pekka.pojo.TbReceivingAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbReceivingAddressMapper {
    int countByExample(TbReceivingAddressExample example);

    int deleteByExample(TbReceivingAddressExample example);

    int deleteByPrimaryKey(Integer addressId);

    int insert(TbReceivingAddress record);

    int insertSelective(TbReceivingAddress record);

    List<TbReceivingAddress> selectByExample(TbReceivingAddressExample example);

    TbReceivingAddress selectByPrimaryKey(Integer addressId);

    int updateByExampleSelective(@Param("record") TbReceivingAddress record, @Param("example") TbReceivingAddressExample example);

    int updateByExample(@Param("record") TbReceivingAddress record, @Param("example") TbReceivingAddressExample example);

    int updateByPrimaryKeySelective(TbReceivingAddress record);

    int updateByPrimaryKey(TbReceivingAddress record);
}