package com.coriander.rabbit.producer.entity;

import java.io.Serializable;
import java.util.Date;

import com.coriander.rabbit.api.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * $BrokerMessage 投递消息记录
 *
 * @author coriander
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokerMessage implements Serializable {
    /**
     * 主键
     */
    private String messageId;

    /**
     * 消息实际内容
     */
    private Message message;

    /**
     * 最大尝试次数
     */
    private Integer tryCount;

    /**
     * 消息状态:0待确认；1投递成功；2投递失败
     */
    private String status;

    /**
     * 下次一次尝试时间
     */
    private Date nextRetry;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}