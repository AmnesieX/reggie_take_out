package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.entity.ShoppingCart;
import com.amnesie.reggie.mapper.ShoppingCartMapper;
import com.amnesie.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-08
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
