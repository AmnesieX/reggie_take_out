package com.amnesie.reggie.service;

import com.amnesie.reggie.dto.SetmealDto;
import com.amnesie.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-04
 */
public interface SetmealService extends IService<Setmeal> {
    //分别保存套餐与套餐菜品到两张表
    public void saveSetmealAndDish(SetmealDto setmealDto);

    //根据id查找套餐及相关菜品
    public SetmealDto getSetmealAndDish(Long id);

    //根据id删除套餐及相关菜品
    public void deleteSetmealAndDish(List<Long> ids);
}
