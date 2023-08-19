package com.coriander.rabbit.api;

/**
 * $SendCallback 回调函数预处理
 *
 * @author coriander
 */
public interface SendCallback {
    void onSuccess();

    void onFailure();
}
