package com.cjluo.rpc.remote.bio;

import com.cjluo.rpc.annotation.RemoteService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务发布，通过Spring管理
 *
 * @author cj.luo
 * @date 2019/4/10
 */
public class PublishService implements ApplicationContextAware, InitializingBean {

    /**
     * 对外发布的服务Map
     */
    private Map<String, Object> serviceHandlerMap = new HashMap<>();

    private final int port;

    public PublishService(int port) {
        this.port = port;
    }

    /**
     * 通过线程池，提高请求的处理能力
     */
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void afterPropertiesSet() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("服务监听启动，端口号：" + this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessHandler(socket, serviceHandlerMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //解析注解，将注解RemoteService的服务保存
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RemoteService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object service : serviceBeanMap.values()) {
                RemoteService remoteService = service.getClass().getAnnotation(RemoteService.class);
                String className = remoteService.value().getName();
                serviceHandlerMap.put(className, service);
            }
        }

    }

}
