package com.amnesie.reggie.mapper;

import com.amnesie.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-02
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
