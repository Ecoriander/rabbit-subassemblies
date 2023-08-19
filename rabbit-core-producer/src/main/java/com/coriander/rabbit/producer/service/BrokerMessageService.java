package com.coriander.rabbit.producer.service;

import com.coriander.rabbit.producer.entity.BrokerMessage;

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
}
