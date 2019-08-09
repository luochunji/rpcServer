package com.cjluo.rpc.remote;

import com.cjluo.rpc.annotation.RemoteService;
import com.cjluo.rpc.api.IPayService;
import com.cjluo.rpc.dto.User;

/**
 * @author cj.luo
 * @date 2019/4/10
 */
@RemoteService(IPayService.class)
public class PayServiceImpl implements IPayService {
    @Override
    public String doPay(User user) {
        String result = String.format("%s 支付成功", user.getName());
        System.out.println(result);
        return result;
    }
}
