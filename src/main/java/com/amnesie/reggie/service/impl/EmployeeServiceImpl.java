package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.entity.Employee;
import com.amnesie.reggie.mapper.EmployeeMapper;
import com.amnesie.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-02
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
