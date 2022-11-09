package com.amnesie.reggie.mapper;

import com.amnesie.reggie.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-09
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
