package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    //批量插入菜品口味数据

    void insertList(List<DishFlavor> dishFlavors);

    //根据菜品id删除口味数据

    void deleteByIds(List<Long> ids);
}
