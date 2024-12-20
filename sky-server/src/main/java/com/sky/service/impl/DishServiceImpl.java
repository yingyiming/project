package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //新增菜品和口味
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表里插入数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        //获取菜品id
        Long dishId = dish.getId();

        //向菜品口味表里插入数据
        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        if(dishFlavors!=null&& !dishFlavors.isEmpty()){
             for (DishFlavor dishFlavor : dishFlavors) {
                 dishFlavor.setDishId(dishId);
             }
            dishFlavorMapper.insertList(dishFlavors);
        }
    }

    //分页查询菜品
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.pageQuery(dishPageQueryDTO);
        long total = dishPage.getTotal();
        List<DishVO> result = dishPage.getResult();
        return new PageResult(total, result);
    }

    @Override
    public void delete(List<Long> ids) {
        //先判断是否是启售状态
        List<Dish> dishList=dishMapper.selectByIds(ids);
        for (Dish dish : dishList) {
            if(dish.getStatus()==1){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断是否和套餐表相关联
        List<Long> dishIds=dishMapper.getSetmealDishIds(ids);
        if(dishIds!=null&&dishIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品和口味
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByIds(ids);
    }
}
