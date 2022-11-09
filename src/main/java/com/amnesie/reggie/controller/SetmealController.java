package com.amnesie.reggie.controller;

import com.amnesie.reggie.common.R;
import com.amnesie.reggie.dto.SetmealDto;
import com.amnesie.reggie.entity.Category;
import com.amnesie.reggie.entity.Setmeal;
import com.amnesie.reggie.service.CategoryService;
import com.amnesie.reggie.service.SetmealDishService;
import com.amnesie.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 套餐管理
 * @author: Amnesie
 * @Date: 2022-10-06
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController  {
    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @RequestMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐添加：{}", setmealDto);
        setmealService.saveSetmealAndDish(setmealDto);
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize ,String name){
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);


        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        queryWrapper.like(name != null, Setmeal::getName, name);
        //执行查询
        setmealService.page(pageInfo, queryWrapper);

        //解决套餐分类不显示问题
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(pageInfo, dtoPage);
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto dishDto = new SetmealDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealAndDish(id);
        return R.success(setmealDto);
    }

    /**
     * 根据id(数组)删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteSetmealAndDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() == 1, Setmeal::getStatus, 1);
        //查询
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
