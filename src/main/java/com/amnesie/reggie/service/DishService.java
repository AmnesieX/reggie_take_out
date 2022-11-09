package com.amnesie.reggie.service;

import com.amnesie.reggie.dto.DishDto;
import com.amnesie.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-04
 */
public interface DishService extends IService<Dish> {
    //新增菜品，同时向dish 与 dishFlavor表添加数据
    public void saveDishAndFlavor(DishDto dishDto);

    //根据id查找菜品信息与口味
    public DishDto getDishAndFlavorById(Long id);

    //更新菜品与口味
    public void updateDishAndFlavor(DishDto dishDto);
}
