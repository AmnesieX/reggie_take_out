package com.amnesie.reggie.service;

import com.amnesie.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-09
 */
public interface OrderService extends IService<Orders> {

    //用户下单功能
    public void submit(Orders orders);
}
