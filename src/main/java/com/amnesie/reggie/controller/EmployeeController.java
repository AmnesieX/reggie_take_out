package com.amnesie.reggie.controller;

import com.amnesie.reggie.common.R;
import com.amnesie.reggie.entity.Employee;
import com.amnesie.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 员工管理
 * @author: Amnesie
 * @Date: 2022-10-02
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
        @PostMapping("/login")
        public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
            //1、将页面提交的密码password进行md5加密处理
            String password = employee.getPassword();
            password = DigestUtils.md5DigestAsHex(password.getBytes());

            //2、根据页面提交的用户名username查询数据库
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getUsername,employee.getUsername());
            Employee emp = employeeService.getOne(queryWrapper);
            log.info(emp.toString());

            //3、如果没有查询到则返回登录失败结果
            if(emp == null){
                return R.error("用户不存在或密码错误!");
            }

            //4、密码比对，如果不一致则返回登录失败结果
            if (!emp.getPassword().equals(password)){
                return R.error("用户不存在或密码错误!");
            }

            //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
            if (emp.getStatus() == 0){
                return R.error("该用户已被禁用!");
            }
            //6、登录成功，将员工id存入Session并返回登录成功结果
            request.getSession().setAttribute("employee",emp.getId());
            //获取用户ip地址
            String remoteHost = request.getRemoteHost();
            log.info("当前访问用户IP地址为：{}", remoteHost);
            //获取cookie
            Cookie[] cookies = request.getCookies();
            log.info("输出用户cookies:{}", cookies.toString());

            return R.success(emp);
        }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
        public R<String> logout(HttpServletRequest request){
            //清理Session中保存的当前登录员工的id
            request.getSession().removeAttribute("employee");
            return R.success("已退出.");
        }


    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
        public R<String> save(@RequestBody Employee employee){
            log.info("新增用户信息:{}",employee.toString());

            //对密码进行加密
            String password = employee.getPassword();
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            employee.setPassword(password);
            //自动填补空缺信息
            //employee.setCreateTime(LocalDateTime.now());
            //employee.setUpdateTime(LocalDateTime.now());

            //Long empId = (Long) request.getSession().getAttribute("employee");
            //employee.setCreateUser(empId);
            //employee.setUpdateUser(empId);

            //保存到数据库
            employeeService.save(employee);
            return R.success("保存成功");
        }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
        public R<Page> page(int page, int pageSize, String name){
            //构造分页构造器
            Page pageInfo = new Page(page, pageSize);
            //构造条件构造器
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
            //添加过滤条件
            queryWrapper.like(StringUtils.hasText(name), Employee::getName, name);
            //添加排序条件
            queryWrapper.orderByDesc(Employee::getUpdateTime);
            //执行查询
            employeeService.page(pageInfo, queryWrapper);
            log.info("pageInfo结果：{}", pageInfo.getTotal());
            return R.success(pageInfo);
        }

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    @PutMapping
        public R<String> update(@RequestBody Employee employee){
            //设置更新时间及更新用户
            //Long empId = (Long)request.getSession().getAttribute("employee");
            //employee.setUpdateUser(empId);
            //employee.setUpdateTime(LocalDateTime.now());
            employeeService.updateById(employee);
            return R.success("用户更新成功");
        }

    /**
     * 信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
        public R<Employee> getById(@PathVariable Long id){
            Employee employee = employeeService.getById(id);
            if (employee != null){
                employee.setPassword(null);
                return R.success(employee);
            }
            return R.error("未找到用户信息");
        }
}
