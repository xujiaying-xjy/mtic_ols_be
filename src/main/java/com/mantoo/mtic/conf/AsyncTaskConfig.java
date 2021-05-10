package com.mantoo.mtic.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置
 *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncTaskConfig implements AsyncConfigurer {

    /**
     * 核心线程数
     */
    private static final int corePoolSize = 5;

    /**
     * 最大线程数-采用CPU驱动模式
     */
    private static final int maximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 多余线程存活时间
     */
    private static final int keepAliveTime = 5;

    /**
     * 线程队列数
     */
    private static final int queueNum = 5;

    /**
     * 拒绝策略-丢弃策略
     */
    private static final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(corePoolSize);
        // 线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(maximumPoolSize);
        // 缓存队列
        taskExecutor.setQueueCapacity(queueNum);
        taskExecutor.setKeepAliveSeconds(keepAliveTime);
        // 对拒绝task的处理策略，当pool已经达到max size的时候，如何处理新任务
        //(1) 默认的ThreadPoolExecutor.AbortPolicy   处理程序遭到拒绝将抛出运行时RejectedExecutionException;
        //(2) ThreadPoolExecutor.CallerRunsPolicy 线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
        //(3) ThreadPoolExecutor.DiscardPolicy  不能执行的任务将被删除;
        //(4) ThreadPoolExecutor.DiscardOldestPolicy  如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）
        taskExecutor.setRejectedExecutionHandler(handler);
        // 线程名前缀,方便排查问题
        taskExecutor.setThreadNamePrefix("ecloud-task-");
        // 初始化
        taskExecutor.initialize();

        return taskExecutor;
    }
}
