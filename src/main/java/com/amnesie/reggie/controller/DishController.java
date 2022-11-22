package com.amnesie.reggie.controller;

import com.amnesie.reggie.common.R;
import com.amnesie.reggie.dto.DishDto;
import com.amnesie.reggie.entity.Category;
import com.amnesie.reggie.entity.Dish;
import com.amnesie.reggie.entity.DishFlavor;
import com.amnesie.reggie.service.CategoryService;
import com.amnesie.reggie.service.DishFlavorService;
import com.amnesie.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 菜品管理
 * @author: Amnesie
 * @Date: 2022-10-05
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CacheManager cacheManager;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveDishAndFlavor(dishDto);
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
        //分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dtoPageInfo = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);

        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行查找
        dishService.page(pageInfo, queryWrapper);

        //根据菜品查找菜品分类
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dtoPageInfo.setRecords(list);

        return R.success(dtoPageInfo);
    }

    /**
     * 信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getDishAndFlavorById(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateDishAndFlavor(dishDto);
        return R.success("保存成功");
    }

    /**
     * 菜品信息回显
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        //添加条件
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus, 1);
//        //添加排序
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//        //执行查询
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }

    /**
     * 菜品信息回显Dto
     * @param dish
     * @return
     */
    @Cacheable(value = "dish", key = "'category_' + #dish.categoryId" )
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行查询
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据分类id查询菜品分类名称
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //根据id查询菜品口味
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
            flavorQueryWrapper.in(DishFlavor::getDishId, dishId);
            List<DishFlavor> flavorList = dishFlavorService.list(flavorQueryWrapper);
            dishDto.setFlavors(flavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dtoList);
    }
}
