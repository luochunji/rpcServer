package com.cjluo.rpc.api;

import com.cjluo.rpc.dto.User;

/**
 * @author cj.luo
 * @date 2019/4/10
 */
public interface IPayService {

    String doPay(User user);
}
