package com.amnesie.reggie.controller;

import com.amnesie.reggie.common.BaseContext;
import com.amnesie.reggie.common.R;
import com.amnesie.reggie.entity.ShoppingCart;
import com.amnesie.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-08
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        log.info("当前用户id为：{}", userId);
        shoppingCart.setUserId(userId);

        //查询当前菜品或套餐是否已经在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getDishFlavor() != null, ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        ShoppingCart currentItem = shoppingCartService.getOne(queryWrapper);
        //如已存在，则将该菜品数量+1
        if (currentItem != null){
            Integer number = currentItem.getNumber();
            currentItem.setNumber(number + 1);
            shoppingCartService.updateById(currentItem);
            return R.success(currentItem);
        }
        //如不存在，则添加到购物车中，默认数量1
        else{
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
    }

    /**
     * 展示购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("展示购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 减少菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        //获取当前用户
        Long userId = BaseContext.getCurrentId();
        log.info("减少菜品+{}", userId);
        //查询菜品或套餐
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getDishFlavor() != null, ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        ShoppingCart currentItem = shoppingCartService.getOne(queryWrapper);
        //获取当前菜品或套餐数量
        Integer number = currentItem.getNumber();
        //菜品数量为1，则删除菜品
        if (number==1){
            shoppingCartService.removeById(currentItem);
        }
        //菜品数量大于1，则将数量-1
        else{
            currentItem.setNumber(number - 1);
            shoppingCartService.updateById(currentItem);
        }
        return R.success("已删除");
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        log.info("用户{}清空购物车" + userId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        //执行清空
        shoppingCartService.remove(queryWrapper);
        return R.success("已清空");
    }
}
