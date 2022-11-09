package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.entity.User;
import com.amnesie.reggie.mapper.UserMapper;
import com.amnesie.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
