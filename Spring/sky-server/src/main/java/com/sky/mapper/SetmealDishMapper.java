package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    //插入套餐菜品
    @AutoFill(value = OperationType.INSERT)
    void insertList(List<SetmealDish> setmealDishes);

    //删除套餐菜品表
    void deleteByIds(List<Long> ids);

    //根据套餐id查询套餐菜品
    List<SetmealDish> getBySetmealId(Long id);
}
