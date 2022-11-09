package com.amnesie.reggie.service;

import com.amnesie.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-04
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
