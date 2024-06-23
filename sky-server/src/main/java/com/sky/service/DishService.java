package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface DishService {


    /**
     * 分页查询菜品
     * @param dishPageQuery
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQuery);
    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

}