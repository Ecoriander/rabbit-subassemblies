package com.coriander.rabbit.producer.service.impl;

import com.coriander.rabbit.producer.constant.BrokerMessageStatus;
import com.coriander.rabbit.producer.entity.BrokerMessage;
import com.coriander.rabbit.producer.mapper.BrokerMessageMapper;
import com.coriander.rabbit.producer.service.BrokerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * $BrokerMessageServiceImpl
 *
 * @author coriander
 */
@Service
public class BrokerMessageServiceImpl implements BrokerMessageService {

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    @Override
    public int insertSelective(BrokerMessage brokerMessage) {
        return brokerMessageMapper.insertSelective(brokerMessage);
    }

    @Override
    public BrokerMessage selectByMessageId(String messageId) {
        return brokerMessageMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public void success(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    @Override
    public void failure(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
    }

    @Override
    public List<BrokerMessage> listTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus) {
        return brokerMessageMapper.queryBrokerMessageStatus4Timeout(brokerMessageStatus.getCode());
    }

    @Override
    public int updateTryCount(String brokerMessageId) {
        return brokerMessageMapper.update4TryCount(brokerMessageId, new Date());
    }
}
