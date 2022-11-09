package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.common.CustomException;
import com.amnesie.reggie.dto.SetmealDto;
import com.amnesie.reggie.entity.Setmeal;
import com.amnesie.reggie.entity.SetmealDish;
import com.amnesie.reggie.mapper.SetmealMapper;
import com.amnesie.reggie.service.SetmealDishService;
import com.amnesie.reggie.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private SetmealDishService setmealDishService;


    /**
     * 分别保存套餐与套餐菜品到两张表
     * @param setmealDto
     */
    @Transactional
    public void saveSetmealAndDish(SetmealDto setmealDto){
        //保存套餐信息，setmeal, insert
        this.save(setmealDto);

        //遍历菜品，为每个菜品setmealId赋值
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        //保存套餐关联菜品信息，setmeal_dish, insert
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 根据id查找套餐及相关菜品
     * @param id
     * @return
     */
    @Override
    public SetmealDto getSetmealAndDish(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        //从setmeal表查询菜品信息
        Setmeal setmeal = this.getById(id);
        //从setmealDish表查询口味
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> dishs = setmealDishService.list(queryWrapper);

        //将套餐信息与菜品拷贝到setmealDto中
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(dishs);
        return setmealDto;
    }

    /**
     * 根据id删除套餐及相关菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteSetmealAndDish(List<Long> ids) {
        //查询套餐状态，只有停售的套餐可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        //不能删除，抛出业务异常
        if (count > 0){
            throw new CustomException("套餐为启用状态，不能删除");
        }

        //删除套餐表中的数据, setmeal, delete
        this.removeByIds(ids);
        //删除关系表中的数据, setmeal_dish, delete
        LambdaQueryWrapper<SetmealDish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(dishQueryWrapper);
    }
}
