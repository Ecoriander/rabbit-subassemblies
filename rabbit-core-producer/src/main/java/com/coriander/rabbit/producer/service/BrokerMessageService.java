package com.coriander.rabbit.producer.service;

import com.coriander.rabbit.producer.constant.BrokerMessageStatus;
import com.coriander.rabbit.producer.entity.BrokerMessage;

import java.util.List;

/**
 * $BrokerMessageService
 *
 * @author coriander
 */
public interface BrokerMessageService {

    int insertSelective(BrokerMessage brokerMessage);

    BrokerMessage selectByMessageId(String messageId);

    void success(String messageId);

    void failure(String messageId);

    List<BrokerMessage> listTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus);

    int updateTryCount(String brokerMessageId);
}
