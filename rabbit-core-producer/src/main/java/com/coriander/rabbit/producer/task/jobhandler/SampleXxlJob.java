package com.coriander.rabbit.producer.task.jobhandler;

import com.coriander.rabbit.producer.broker.RabbitBroker;
import com.coriander.rabbit.producer.constant.BrokerMessageStatus;
import com.coriander.rabbit.producer.entity.BrokerMessage;
import com.coriander.rabbit.producer.service.BrokerMessageService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * $SampleXxlJob
 * XxlJob开发示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、任务开发：在Spring Bean实例中，开发Job方法；
 * 2、注解配置：为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobHelper.log" 打印执行日志；
 * 4、任务结果：默认任务结果为 "成功" 状态，不需要主动设置；如有诉求，比如设置任务结果为失败，可以通过 "XxlJobHelper.handleFail/handleSuccess" 自主设置任务结果；
 *
 * @author coriander
 */
@Slf4j
@Component
public class SampleXxlJob {

    @Autowired
    private BrokerMessageService brokerMessageService;
    @Autowired
    private RabbitBroker rabbitBroker;

    private static final int MAX_RETRY_COUNT = 3;

    @XxlJob("retryMessageHandler")
    public void retryMessageHandler() throws Exception {
        List<BrokerMessage> list = brokerMessageService.listTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("-------- 获取数据集合, 数量：	{} 	-----------", list.size());

        list.forEach(brokerMessage -> {
            String messageId = brokerMessage.getMessageId();
            if (brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
                this.brokerMessageService.failure(messageId);
                log.warn(" -----消息设置为最终失败，消息ID: {} -------", messageId);
            } else {
                //	每次重发的时候要更新一下try count字段
                brokerMessageService.updateTryCount(messageId);
                // 	重发消息
                rabbitBroker.reliantSend(brokerMessage.getMessage());
            }
        });
    }
}
