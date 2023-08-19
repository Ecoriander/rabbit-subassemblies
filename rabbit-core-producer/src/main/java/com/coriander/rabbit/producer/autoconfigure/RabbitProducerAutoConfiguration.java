package com.coriander.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * $RabbitProducerAutoConfiguration 自动装配
 *
 * @author coriander
 */
@Configuration
@ComponentScan({"com.coriander.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {
}
