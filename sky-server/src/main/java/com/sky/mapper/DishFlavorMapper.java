package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishFlavorMapper {
    //批量插入菜品口味数据

    void insertList(List<DishFlavor> dishFlavors);

    //根据菜品id删除口味数据

    void deleteByIds(List<Long> ids);

    @Select("select * from dish_flavor where dish_id = #{id}")
    ArrayList<DishFlavor> selectByDishId(Long id);


}
