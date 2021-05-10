package com.mantoo.mtic.conf;

import com.mantoo.mtic.module.netty.tcp.TcpServer;
import com.mantoo.mtic.module.netty.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动执行方法
 *
 * @author keith
 * @version 1.0
 * @date 2021-03-03
 */
@Slf4j
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Value("${netty.port.tcp}")
    private Integer tcpPort;
    @Value("${netty.port.ws}")
    private Integer wsPort;

//    @Value("${netty.port.lampblack}")
//    private Integer lampblackPort;

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        log.debug("=======系统启动========");
        new TcpServer().run(tcpPort);
        new WebSocketServer().run(wsPort);
//        new LampblackServer().run(lampblackPort);
    }
}