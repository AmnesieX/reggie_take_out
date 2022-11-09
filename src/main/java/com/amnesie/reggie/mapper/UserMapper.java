package com.amnesie.reggie.mapper;

import com.amnesie.reggie.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
