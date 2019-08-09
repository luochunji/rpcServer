package com.cjluo.rpc.remote.nio;

import com.cjluo.rpc.dto.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author cj.luo
 * @date 2019/4/10
 */
public class ProcessHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> serviceHandlerMap;

    public ProcessHandler(Map<String, Object> serviceHandlerMap) {
        this.serviceHandlerMap = serviceHandlerMap;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = parseAndInvoke((Request) msg);
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    /**
     * 请求解析、服务方法调用
     *
     * @param request
     * @return
     */
    private Object parseAndInvoke(Request request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String className = request.getClassName();
        Object service = serviceHandlerMap.get(className);
        if (null == service) {
            throw new RuntimeException("service not found:" + className);
        }
        Object[] params = request.getParams();
        Class<?>[] types = null;
        if (null != params) {
            types = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                types[i] = params[i].getClass();
            }
        }
        String methodName = request.getMethodName();
        Class clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName, types);
        return method.invoke(service, params);
    }
}
