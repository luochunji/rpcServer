package com.cjluo.rpc.remote.bio;

import com.cjluo.rpc.dto.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author cj.luo
 * @date 2019/4/10
 */
public class ProcessHandler implements Runnable {

    private Socket socket;

    private Map<String, Object> serviceHandlerMap;

    public ProcessHandler(Socket socket, Map<String, Object> serviceHandlerMap) {
        this.socket = socket;
        this.serviceHandlerMap = serviceHandlerMap;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            //读取客户端的请求流
            ois = new ObjectInputStream(socket.getInputStream());
            Request request = (Request) ois.readObject();
            //服务端处理请求
            Object result = parseAndInvoke(request);
            //返回请求结果
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (null != ois) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != oos) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
