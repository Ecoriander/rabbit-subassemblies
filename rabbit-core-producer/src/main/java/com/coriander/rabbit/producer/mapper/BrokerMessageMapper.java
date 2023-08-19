package com.coriander.rabbit.producer.mapper;

import com.coriander.rabbit.producer.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * $BrokerMessageMapper
 *
 * @author coriander
 */
@Mapper
public interface BrokerMessageMapper {
    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessage record);

    int insertSelective(BrokerMessage record);

    BrokerMessage selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessage record);

    int updateByPrimaryKey(BrokerMessage record);

    void changeBrokerMessageStatus(@Param("brokerMessageId")String brokerMessageId, @Param("brokerMessageStatus")String brokerMessageStatus, @Param("updateTime") Date updateTime);
}