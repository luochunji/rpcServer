package com.cjluo.rpc.dto;

import java.io.Serializable;

/**
 * 服务请求对象，保存请求的类名、方法名和请求入参对象
 *
 * @author cj.luo
 * @date 2019/4/10
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 4512515646445414036L;

    private String className;

    private String methodName;

    private Object[] params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
