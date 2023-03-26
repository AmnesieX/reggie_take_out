package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.dto.DishDto;
import com.amnesie.reggie.entity.Dish;
import com.amnesie.reggie.entity.DishFlavor;
import com.amnesie.reggie.mapper.DishMapper;
import com.amnesie.reggie.service.DishFlavorService;
import com.amnesie.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-04
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时向dish 与 dishFlavor表添加数据
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveDishAndFlavor(DishDto dishDto) {
        //保存基本信息到dish
        this.save(dishDto);

        //获取口味所对应的dishId
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存口味到dishFlavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查找菜品信息与口味
     * @param id
     * @return
     */
    @Override
    public DishDto getDishAndFlavorById(Long id) {
        DishDto dishDto = new DishDto();
        //从dish表查询菜品信息
        Dish dish = this.getById(id);
        //从dishFlavor表查询口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavor = dishFlavorService.list(queryWrapper);

        //将菜品信息与口味拷贝到dishDto中
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavor);
        return dishDto;
    }

    /**
     * 更新菜品与口味
     * @param dishDto
     */
    @Override
    public void updateDishAndFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据--dish_flavor表的insert操件
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
