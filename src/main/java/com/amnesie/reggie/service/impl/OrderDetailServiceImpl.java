package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.entity.OrderDetail;
import com.amnesie.reggie.mapper.OrderDetailMapper;
import com.amnesie.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-09
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
