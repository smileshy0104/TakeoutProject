package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
    /**
     * 分页查询菜品
     * @param dishPageQuery
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQuery);

    /**
     * 根据id查询菜品和对应的口味数据
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id);


    /**
     * 修改菜品和对应的口味数据
     *
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO);

    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);


    /**
     * 菜品批量删除
     *
     * @param ids
     */
    public void deleteBatch(List<Long> ids);
}