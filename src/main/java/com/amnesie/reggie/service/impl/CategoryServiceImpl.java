package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.common.CustomException;
import com.amnesie.reggie.entity.Category;
import com.amnesie.reggie.entity.Dish;
import com.amnesie.reggie.entity.Setmeal;
import com.amnesie.reggie.mapper.CategoryMapper;
import com.amnesie.reggie.service.CategoryService;
import com.amnesie.reggie.service.DishService;
import com.amnesie.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-04
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        //查询当前分类是否关联菜品，如关联则抛出业务异常
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0){
            //关联到菜品，抛出异常
            throw new CustomException("删除失败，当前分类已关联菜品!");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        //查询当前套餐是否关联菜品，如关联则抛出业务异常
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0){
            //关联到套餐，抛出异常
            throw new CustomException("删除失败，当前分类已关联套餐!");
        }
        //执行删除
    }
}
