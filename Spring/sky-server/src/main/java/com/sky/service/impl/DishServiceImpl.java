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

    //根据ID查询菜品
    @Override
    public DishVO getById(Long id) {
        ArrayList<DishFlavor> dishFlavors=dishFlavorMapper.selectByDishId(id);
        Dish dish=dishMapper.selectById(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setCategoryName(dishMapper.getCategoryNameById(dish.getCategoryId()));
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    //启售和停售菜品
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish=Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    //修改菜品
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        //修改菜品表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //修改菜品口味表
        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        List<Long> id=new ArrayList<>();
        id.add(dishDTO.getId());
        if(dishFlavors!=null&& !dishFlavors.isEmpty()){
            dishFlavorMapper.deleteByIds(id);
            dishFlavorMapper.insertList(dishFlavors);
        }
    }

    @Override
    public List<DishVO> list(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    //条件查询菜品和口味
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<DishVO> dishVOList = dishMapper.getByCategoryId(dish.getCategoryId());
        //查询菜品口味
        for (DishVO d : dishVOList) {
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());
            d.setFlavors(flavors);
        }

        return dishVOList;

    }
}
