package com.amnesie.reggie.controller;

import com.amnesie.reggie.common.R;
import com.amnesie.reggie.entity.User;
import com.amnesie.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户管理
 * @author: Amnesie
 * @Date: 2022-10-08
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request, @RequestBody User user){
        //接收密码，进行md5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User tuser = userService.getOne(queryWrapper);

        //如果没有查询到则返回登录失败结果
        if (tuser == null){
            return R.error("用户不存在或密码错误!");
        }

        //进行密码比对
        if (!tuser.getPassword().equals(password)){
            return R.error("用户不存在或密码错误!");
        }

        //查看用户状态，如果为已禁用状态，则返回用户已禁用结果
        if (tuser.getStatus() != 1){
            return R.error("该用户已被禁用");
        }

        //登录成功，将用户id存入浏览器Session中，返回登录成功结果
        request.getSession().setAttribute("user", tuser.getId());
        return R.success("登录成功");
    }


}
