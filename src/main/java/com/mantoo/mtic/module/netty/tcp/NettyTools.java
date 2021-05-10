package com.mantoo.mtic.module.netty.tcp;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author keith
 * @version 1.0
 * @date 2021-02-23
 */
@Slf4j
public class NettyTools {

    /**
     * 响应消息缓存
     */
    private static final Cache<String, BlockingQueue<byte[]>> responseMsgCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(10000, TimeUnit.SECONDS)
            .build();


    /**
     * 等待响应消息
     *
     * @param key 消息唯一标识
     * @return ReceiveDdcMsgVo
     */
    public static byte[] waitReceiveMsg(String key, Long timeout) {
        if (!Optional.ofNullable(timeout).isPresent()) {
            timeout = 10000L;
        }
        try {
            //设置超时时间
            //poll,轮询，有输入则返回，没有则等待超时时间返回null
            byte[] vo = Objects.requireNonNull(responseMsgCache.getIfPresent(key))
                    .poll(timeout, TimeUnit.MILLISECONDS);

            //删除key
            responseMsgCache.invalidate(key);
            if (Optional.ofNullable(vo).isPresent()) {
                return vo;
            } else {
                log.error("网关超时未回复,sn={}", key);
//                throw new MticException("网关超时未回复", ErrorInfo.GATEWAY_OVERTIME.getCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取数据异常,sn={}",key, e);
            return null;
//            throw new MticException("网关超时未回复", ErrorInfo.GATEWAY_OVERTIME.getCode());
        }
    }

    /**
     * 初始化响应消息的队列
     *
     * @param key 消息唯一标识
     */
    public static void initReceiveMsg(String key) {
        responseMsgCache.put(key, new LinkedBlockingQueue<>(1));
    }

    /**
     * 设置响应消息
     *
     * @param key 消息唯一标识
     */
    public static void setReceiveMsg(String key, byte[] bytes) {
        if (responseMsgCache.getIfPresent(key) != null) {
            Objects.requireNonNull(responseMsgCache.getIfPresent(key)).add(bytes);
            return;
        }
        log.warn("sn {}不存在", key);
    }

}
